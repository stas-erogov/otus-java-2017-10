package dbService;

import dao.AddressesDAOMyORM;
import dao.BaseDAO;
import dao.PhonesDAOMyORM;
import dao.UsersDAOMyORM;
import db.AddressDataSet;
import db.DataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import myorm.ConnectionHelperH2;
import myorm.MyORMExecutor;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBServiceMyORMImpl implements DBService {
    private final MyORMExecutor executor;
    public DBServiceMyORMImpl() {
        executor = new MyORMExecutor(ConnectionHelperH2.getConnection());
        checkSchema();
    }

    @Override
    public String getLocalStatus() {
        return executor.getStatus();
    }

    @Override
    public void save(DataSet dataSet) {
        Type type = dataSet.getClass();
        getDAO(type).save(dataSet);
    }

    @Override
    public DataSet read(long id, Type type) {
        return getDAO(type).read(id);
    }

    @Override
    public <T extends DataSet> List<T> readAll(Type type) {
        return getDAO(type).readAll();
    }

    @Override
    public void delete(DataSet dataSet) {
        Type type = DataSet.class;
        getDAO(type).delete(dataSet);
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("Shutdown not supported");
    }

    private BaseDAO getDAO(Type type) {
        if (type == UserDataSet.class) {
            return new UsersDAOMyORM(executor);
        } else if (type == AddressDataSet.class) {
            return new AddressesDAOMyORM(executor);
        } else if (type == PhoneDataSet.class) {
            return new PhonesDAOMyORM(executor);
        }
        throw new UnsupportedOperationException("Unsupported class: " + type);
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
            boolean isExist = executor.execQuery(sqlCheck, ResultSet::next);
            if (!isExist) {
                executor.execUpdate(sqlCreate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
