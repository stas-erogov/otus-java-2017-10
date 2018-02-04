package dbService;

import cache.CacheService;
import dao.*;
import db.AddressDataSet;
import db.DataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
    private final SessionFactory sessionFactory;

    private final Map<Type, BaseDAOHibernate> daos = new HashMap<>();
    public DBServiceHibernateImpl() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:tcp://localhost/~/test");
        configuration.setProperty("hibernate.connection.username", "sa");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");

        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        sessionFactory = createSessionFactory(configuration);

        daos.put(UserDataSet.class, new UsersDAOHibernate());
        daos.put(AddressDataSet.class, new AddressesDAOHibernate());
        daos.put(PhoneDataSet.class, new PhonesDAOHibernate());
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public String getLocalStatus() {
        return runInSession(session -> session.getTransaction().getStatus().name());
    }

    @Override
    public void save(DataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            getDAO(session, dataSet.getClass()).save(dataSet);
            session.getTransaction().commit();
        }
    }

    @Override
    public DataSet read(long id, Type dataSetType) {
        return runInSession((session, type) -> getDAO(session, type).read(id), dataSetType);
    }

    @Override
    public void delete(DataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            getDAO(session, dataSet.getClass()).delete(dataSet);
            session.getTransaction().commit();
        }
    }

    @Override
    public CacheService getCache() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<DataSet> readAll(Type dataSetType) {
        return runInSession((session, type) -> getDAO(session, type).readAll(), dataSetType);
    }



    @Override
    public void shutdown() {
        sessionFactory.close();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    private <R> R runInSession(BiFunction<Session, Type, R> function, Type type) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session, type);
            transaction.commit();
            return result;
        }
    }

    private BaseDAOHibernate getDAO(Type type) {
        return daos.get(type);
    }

    private BaseDAO getDAO(Session session, Type type) {
        BaseDAOHibernate dao = this.getDAO(type);
        dao.setSession(session);
        return dao;
    }

}
