package dao;

import db.DataSet;
import db.PhoneDataSet;
import myorm.MyORMExecutor;

import java.util.List;

public class PhonesDAOMyORM extends BaseDAOMyORM{

    public PhonesDAOMyORM(MyORMExecutor executor) {
        super(executor);
    }

    @Override
    public DataSet read(long id) {
        return getExecutor().load(id, PhoneDataSet.class);
    }

    @Override
    public List<PhoneDataSet> readAll() {
        return getExecutor().load(PhoneDataSet.class);
    }
}
