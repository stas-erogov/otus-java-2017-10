package dao;

import org.hibernate.Session;

public interface BaseDAOHibernate extends BaseDAO {
    void setSession(Session session);
}
