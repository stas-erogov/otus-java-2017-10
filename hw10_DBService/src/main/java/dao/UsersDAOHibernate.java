package dao;

import db.DataSet;
import db.UserDataSet;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UsersDAOHibernate implements BaseDAOHibernate {
    private Session session;

    public UsersDAOHibernate() {
    }

    @Override
    public void save(DataSet dataSet) {
        session.saveOrUpdate(dataSet);
    }

    @Override
    public UserDataSet read(long id) {
        return session.load(UserDataSet.class, id);
    }

    @Override
    public void delete(DataSet dataSet) {
        session.delete(dataSet);
    }

    @Override
    public List<UserDataSet> readAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteriaQuery = builder.createQuery(UserDataSet.class);
        criteriaQuery.from(UserDataSet.class);
        return session.createQuery(criteriaQuery).list();
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }
}
