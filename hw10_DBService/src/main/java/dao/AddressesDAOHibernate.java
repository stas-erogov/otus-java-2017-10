package dao;

import db.AddressDataSet;
import db.DataSet;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AddressesDAOHibernate implements BaseDAO {
    private final Session session;

    public AddressesDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public void save(DataSet dataSet) {
        session.save(dataSet);
    }

    @Override
    public AddressDataSet read(long id) {
        return session.load(AddressDataSet.class, id);
    }

    @Override
    public void delete(DataSet dataSet) {
        session.delete(dataSet);
    }

    @Override
    public List<AddressDataSet> readAll() {
        session.beginTransaction();
        Query<AddressDataSet> query = session.createQuery("select a from addresses a", AddressDataSet.class);
        session.getTransaction().commit();
        return query.getResultList();
    }
}
