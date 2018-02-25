package dao;

import db.DataSet;

import java.util.List;

public interface BaseDAO {
    void save(DataSet dataSet);

    DataSet read(long id);

    <T extends DataSet> List<T> readAll();

    void delete(DataSet dataSet);
}
