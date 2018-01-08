package dao;

import db.AddressDataSet;
import db.DataSet;
import myorm.MyORMExecutor;

import java.util.List;

public class AddressesDAOMyORM extends BaseDAOMyORM {

    public AddressesDAOMyORM(MyORMExecutor executor) {
        super(executor);
    }

    @Override
    public DataSet read(long id) {
        return getExecutor().load(id, AddressDataSet.class);
    }

    @Override
    public List<AddressDataSet> readAll() {
        return getExecutor().load(AddressDataSet.class);
    }
}
