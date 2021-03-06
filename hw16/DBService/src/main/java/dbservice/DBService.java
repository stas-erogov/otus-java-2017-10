package dbservice;

import db.DataSet;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service("dbService")
public interface DBService {
    String getLocalStatus();

    void save(DataSet dataSet);
    DataSet read(long id, Type type);
    <T extends DataSet> List<T> readAll(Type type);
    void delete(DataSet dataSet);

    void shutdown();
}
