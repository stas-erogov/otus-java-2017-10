package dao;

import db.DataSet;
import myorm.MyORMExecutor;

public abstract class BaseDAOMyORM implements BaseDAO{
    private MyORMExecutor executor;

    BaseDAOMyORM(MyORMExecutor executor) {
        this.executor = executor;
    }

    MyORMExecutor getExecutor() {
        return executor;
    }

    @Override
    public void save(DataSet dataSet) {
        executor.save(dataSet);
    }

    @Override
    public void delete(DataSet dataSet) {
        System.out.println("Delete not supported");
    }
}
