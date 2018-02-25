package dao;

import db.DataSet;
import db.UserDataSet;
import myorm.MyORMExecutor;

import java.util.List;

public class UsersDAOMyORM extends BaseDAOMyORM {
    public UsersDAOMyORM(MyORMExecutor executor) {
        super(executor);
    }

    @Override
    public DataSet read(long id) {
        return getExecutor().load(id, UserDataSet.class);
    }

    @Override
    public List<UserDataSet> readAll() {
        return getExecutor().load(UserDataSet.class);
    }
}
