package dbService;

import cache.CacheService;
import db.DataSet;

import java.lang.reflect.Type;
import java.util.List;

public interface DBService {
    String getLocalStatus();

    void save(DataSet dataSet);
    DataSet read(long id, Type type);
    <T extends DataSet> List<T> readAll(Type type);
    void delete(DataSet dataSet);
    CacheService getCache();

    void shutdown();
}
