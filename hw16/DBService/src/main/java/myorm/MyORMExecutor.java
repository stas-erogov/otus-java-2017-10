package myorm;

import cache.CacheEngine;
import cache.MyElement;
import db.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static myorm.ConnectionHelperH2.STATUS_QUERY;
import static myorm.ReflectionHelper.getFieldValue;

public class MyORMExecutor {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private DataSource dataSource;
    private Connection connection;

    private CacheEngine<Long, DataSet> cacheEngine;

    public MyORMExecutor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Init orm executor error", e);
        }
        checkSchema();
    }

    public void setCacheEngine(CacheEngine<Long, DataSet> cacheEngine) {
        this.cacheEngine = cacheEngine;
    }

    public CacheEngine<Long, DataSet> getCacheEngine() {
        return cacheEngine;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T extends DataSet> void save(T dataSet) {
        this.save(dataSet, null);
    }

    private <T extends DataSet> void save(T dataSet, T parent) {
        checkId(dataSet);

        String sql = getInsert(dataSet.getClass(), dataSet, parent);
        log.info(sql);

        int rows = 0;
        try {
            rows = execUpdate(sql);
        } catch (SQLException e) {
            log.error("execUpdate error", e);
        }
        log.info("Inserted " + rows + " rows");
        cacheEngine.put(dataSet.getId(), new MyElement<>(dataSet));


        Field[] fields = ReflectionHelper.getAnnotatedFields(dataSet.getClass(), OneToMany.class);
        for (Field f : fields) {
            Object children = ReflectionHelper.getFieldValue(dataSet, f);
            if (children != null) {
                if (Collection.class.isAssignableFrom(children.getClass()) ) {
                    for (T child : (Collection<T>) children) {
                        save(child, dataSet);
                    }
                }
            }
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        T queryObject = null;
        if (cacheEngine.get(id) != null) {
            queryObject = (T) cacheEngine.get(id).getValue();
            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());
            return queryObject;
        }

        String sql = getSelect(clazz, id);
        log.info(sql);

        try {
            queryObject = execQuery(sql, resultSet -> loadResultSet(clazz, resultSet, null).get(0));
        } catch (SQLException | IndexOutOfBoundsException e) {
            log.error("execQuery error", e);
        }
        cacheEngine.put(id, new MyElement<>(queryObject));
        return queryObject;
    }


    public <T extends DataSet> List<T> load(Class<T> clazz) {
        String sql = getSelect(clazz, 0);
        log.info(sql);

        List<T> queryObjects = null;
        try {
            queryObjects = execQuery(sql, resultSet -> loadResultSet(clazz, resultSet));
        } catch (SQLException e) {
            log.error("execQuery error", e);
        }
        return queryObjects;
    }

    private <T extends DataSet> List<T> loadResultSet(Class<T> clazz, ResultSet resultSet) throws SQLException {
        return this.loadResultSet(clazz, resultSet, null);
    }

    private <T extends DataSet> List<T> loadResultSet(Class<T> clazz, ResultSet resultSet, T parent) throws SQLException {
        List<Field> fields = ReflectionHelper.getAllFields(clazz);

        List<T> resultObjects = new ArrayList<>();
        while (resultSet.next()) {

            T t = ReflectionHelper.instantiate(clazz);
            for (Field f : fields) {
                String name = getColumnName(f);
                if (f.isAnnotationPresent(OneToOne.class)) {
                    T join = load(resultSet.getLong(name), (Class<T>) f.getType());
                    ReflectionHelper.setFieldValue(t, f, join);

                } else if (f.isAnnotationPresent(Column.class)
                        || f.isAnnotationPresent(JoinColumn.class)
                        ) {
                    if (f.isAnnotationPresent(ManyToOne.class)) {
                        ReflectionHelper.setFieldValue(t, f, parent);
                    } else {
                        ReflectionHelper.setFieldValue(t, f, resultSet.getObject(name));
                    }
                } else if (f.isAnnotationPresent(OneToMany.class)) {
                    Object join = loadChildren(f, t);
                    ReflectionHelper.setFieldValue(t, f, join);
                }
            }
            resultObjects.add(t);
        }
        return resultObjects;
    }

    private <T extends DataSet> Set<T> loadChildren(Field f, T parent) {

        StringBuilder sql = new StringBuilder("select * from ");

        Class<T> targetEntity = f.getAnnotation(OneToMany.class).targetEntity();
        sql.append(getTableName(targetEntity));
        sql.append(" where ").append(getRefColumnName(f)).append(" = ").append(parent.getId());

        log.info(sql.toString());

        Collection<T> queryObject = null;
        try {
            queryObject =  execQuery(sql.toString(), resultSet -> loadResultSet(targetEntity, resultSet, parent));
        } catch (SQLException e) {
            log.error("loadChildren error", e);
        }

        return queryObject != null ? new HashSet<>(queryObject) : null;
    }

    private String getRefColumnName(Field f) {
        String mappedBy = f.getAnnotation(OneToMany.class).mappedBy();
        Class clazz = f.getAnnotation(OneToMany.class).targetEntity();

        String columnName = "";

        try {
             columnName = clazz.getDeclaredField(mappedBy).getAnnotation(JoinColumn.class).name();
        } catch (NoSuchFieldException e) {
            log.error("getRefColumnName error", e);
        }
        return columnName;
    }


    public <T> T execQuery(String sql, ResultHandler<T> handler) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);

        ResultSet resultSet = statement.getResultSet();
        T value = handler.handle(resultSet);

        resultSet.close();
        statement.close();
        return value;
    }

    public int execUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(sql);
        statement.close();
        return rows;
    }

    private String getSelect(Class<?> clazz, long id) {
        if (isEntity(clazz)) {
            StringBuilder sb = new StringBuilder("select * from ");
            String table_name = getTableName(clazz);
            sb.append(table_name).append(" ");
            if (id > 0) {
                sb.append("where ");
                String column_name = "id";
                String operator = "=";
                String value = String.valueOf(id);

                sb.append(column_name).append(" ")
                        .append(operator).append(" ")
                        .append(value);
            }
            return sb.toString();
        }
        throw new EntityNotFoundException();
    }

    private String getTableName(Class<?> clazz) {
        String table_name = clazz.getAnnotation(Entity.class).name();
        table_name = table_name.isEmpty() ? clazz.getSimpleName().toUpperCase() : table_name;
        return table_name;
    }

    private <T extends DataSet> String getInsert(Class<?> clazz, T object, T parent) {
        if (isEntity(clazz)) {
            StringBuilder sql = new StringBuilder("insert into ");
            sql.append(getTableName(clazz));

            Field[] fields = ReflectionHelper.getAllFields(clazz).stream()
                    .filter(f-> f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(JoinColumn.class))
                    .toArray(Field[]::new);
            StringBuilder names = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (Field f : fields) {
                String name = getColumnName(f);
                String value = formatFieldValue(object, f, parent);
                if (names.length()>0) {
                    names.append(",");
                    values.append(",");
                }
                names.append(name);
                values.append(value);
            }

            sql.append("(").append(names).append(") ");
            sql.append("values (").append(values).append(")");

            return sql.toString();
        }
        throw new EntityNotFoundException();
    }

    private String getColumnName(Field f) {
        if (f.isAnnotationPresent(JoinColumn.class)) {
            return f.getAnnotation(JoinColumn.class).name();
        } else if (f.isAnnotationPresent(Column.class)) {
            return f.getAnnotation(Column.class).name();
        } else if (f.isAnnotationPresent(OneToMany.class)){
            return f.getName();
        }
        return null;
    }

    private <T extends DataSet> void checkId(T dataSet) {
        long id;
        if (dataSet.getId() == 0) {
            Field f;
            try {
                f = dataSet.getClass().getSuperclass().getDeclaredField("id");
                id = execQuery("select nextval('"
                        + f.getAnnotation(SequenceGenerator.class).sequenceName()
                        + "') from dual", resultSet -> {
                    resultSet.next();
                    return resultSet.getLong(1);
                });
                dataSet.setId(id);
            } catch (SQLException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    private <T extends DataSet> String formatFieldValue(T object, Field f, T parent) {
        Type type = f.getType();
        Object value = getFieldValue(object, f);
        if (f.isAnnotationPresent(OneToOne.class)) {
            DataSet oneToOneEntity = (DataSet) ReflectionHelper.getFieldValue(object, f);
            if (oneToOneEntity != null) {
                value = oneToOneEntity.getId();
                type = value.getClass();
                if (value.equals(0)) {
                    String message = "Object " + f.getType() + " " + f.getName()
                            + "not exist in RDBMS";
                    log.error(message);
                    throw new RuntimeException(message);
                }
            }
        }

        if (f.isAnnotationPresent(ManyToOne.class)) {
            value = parent.getId();
            type = value.getClass();
        }

        if (type == Integer.class || type == int.class ||
                type == Double.class || type == double.class ||
                type == Long.class || type == long.class ||
                type == Float.class || type == float.class) {

            return value != null ? String.valueOf(value) : "null";
        } else {
            return value != null ? "'" + String.valueOf(value) + "'" : "null";
        }
    }

    public String getStatus() {
        try {
            boolean isActive = execQuery(STATUS_QUERY, ResultSet::next);
            return isActive ? "ACTIVE" : "INACTIVE";
        } catch (SQLException e) {
            log.error(getStatus(), e);
            return "SQL Error: " + e.getSQLState();
        }
    }

    private boolean isEntity (Class<?> clazz) {
        return clazz.isAnnotationPresent(Entity.class);
    }

    public void cacheDispose() {
        cacheEngine.dispose();
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Closing executor error", e);
        }
    }


    private void checkSchema() {
        checkSchemaObject(
                "SELECT 1 FROM INFORMATION_SCHEMA.SEQUENCES " +
                        "  WHERE SEQUENCE_SCHEMA = 'PUBLIC' AND SEQUENCE_NAME = 'DATASET_SEQ'",
                "CREATE SEQUENCE PUBLIC.DATASET_SEQ START WITH 1");
        checkSchemaObject(
                "SELECT 1 FROM INFORMATION_SCHEMA.TABLES" +
                        "  WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_NAME = 'ADDRESSES'",
                "CREATE CACHED TABLE PUBLIC.ADDRESSES(\n" +
                        "     ID BIGINT NOT NULL,\n" +
                        "     ADDRESS VARCHAR(255),\n" +
                        "     POSTCODE VARCHAR(255)\n" +
                        "     ); \n" +
                        "ALTER TABLE PUBLIC.ADDRESSES ADD CONSTRAINT PUBLIC.ADDR_PK PRIMARY KEY(ID);\n");
        checkSchemaObject(
                "SELECT 1 FROM INFORMATION_SCHEMA.TABLES" +
                        "  WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_NAME = 'USERS'",
                "CREATE CACHED TABLE PUBLIC.USERS(\n" +
                        "     ID BIGINT NOT NULL,\n" +
                        "     AGE INTEGER,\n" +
                        "     NAME VARCHAR(255),\n" +
                        "     ADDRESS_ID BIGINT NOT NULL\n" +
                        "     );\n " +
                        "ALTER TABLE PUBLIC.USERS ADD CONSTRAINT PUBLIC.USER_PK PRIMARY KEY(ID);\n" +
                        "ALTER TABLE PUBLIC.USERS ADD CONSTRAINT PUBLIC.USER_ADDR_UK UNIQUE(ADDRESS_ID);\n" +
                        "ALTER TABLE PUBLIC.USERS ADD CONSTRAINT PUBLIC.USER_ADDR_FK" +
                        " FOREIGN KEY(ADDRESS_ID) REFERENCES PUBLIC.ADDRESSES(ID) NOCHECK;"
        );
        checkSchemaObject(
                "SELECT 1 FROM INFORMATION_SCHEMA.TABLES" +
                        "  WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_NAME = 'PHONES'",
                "CREATE CACHED TABLE PUBLIC.PHONES(\n" +
                        "     ID BIGINT NOT NULL,\n" +
                        "     PHONE_NUM VARCHAR(255),\n" +
                        "     OWNER_ID BIGINT\n" +
                        "     ); " +
                        "ALTER TABLE PUBLIC.PHONES ADD CONSTRAINT PUBLIC.PHONE_PK PRIMARY KEY(ID);\n " +
                        "ALTER TABLE PUBLIC.PHONES ADD CONSTRAINT PUBLIC.PHONE_USER_FK" +
                        " FOREIGN KEY(OWNER_ID) REFERENCES PUBLIC.USERS(ID) NOCHECK;"
        );
    }

    private void checkSchemaObject(String sqlCheck, String sqlCreate) {
        try {
            boolean isExist;
            isExist = this.execQuery(sqlCheck, ResultSet::next);
            if (!isExist) {
                this.execUpdate(sqlCreate);
            }
        } catch (SQLException e) {
            log.error("check schema error", e);
        }
    }
}
