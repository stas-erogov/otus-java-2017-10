package dao;

import db.DataSet;
import db.PhoneDataSet;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PhonesDAOHibernate implements BaseDAOHibernate {
    private Session session;

    public PhonesDAOHibernate() {
    }

    @Override
    public void save(DataSet dataSet) {
        session.save(dataSet);
    }

    @Override
    public DataSet read(long id) {
        return session.load(PhoneDataSet.class, id);
    }

    @Override
    public void delete(DataSet dataSet) {
        session.delete(dataSet);
    }

    @Override
    public List<PhoneDataSet> readAll() {
//        session.beginTransaction();
        Query<PhoneDataSet> query = session.createQuery("select p from phones p", PhoneDataSet.class);
//        session.getTransaction().commit();
        return query.getResultList();
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }
}
