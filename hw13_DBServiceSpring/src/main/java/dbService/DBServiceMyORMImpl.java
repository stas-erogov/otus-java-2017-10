package dbService;

import dao.AddressesDAOMyORM;
import dao.BaseDAO;
import dao.PhonesDAOMyORM;
import dao.UsersDAOMyORM;
import db.AddressDataSet;
import db.DataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import myorm.MyORMConfig;
import myorm.MyORMExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBServiceMyORMImpl implements DBService {
    Logger log = LoggerFactory.getLogger("Common");
    private final Map<Type, BaseDAO> daos = new HashMap<>();

    private MyORMConfig myORMConfig;
    private MyORMExecutor myORMExecutor;

    public DBServiceMyORMImpl(MyORMConfig myORMConfig) {
        this.myORMConfig = myORMConfig;
    }

    public void init() {
        daos.put(UserDataSet.class, new UsersDAOMyORM(myORMExecutor));
        daos.put(AddressDataSet.class, new AddressesDAOMyORM(myORMExecutor));
        daos.put(PhoneDataSet.class, new PhonesDAOMyORM(myORMExecutor));
    }

    public MyORMExecutor getMyORMExecutor() {
        return myORMExecutor;
    }

    public void setMyORMExecutor(MyORMExecutor myORMExecutor) {
        this.myORMExecutor = myORMExecutor;
    }

    public MyORMConfig getMyORMConfig() {
        return myORMConfig;
    }

    @Override
    public String getLocalStatus() {
        return myORMExecutor.getStatus();
    }

    @Override
    public void save(DataSet dataSet) {
        Type type = dataSet.getClass();
        getDAO(type).save(dataSet);
    }

    @Override
    public DataSet read(long id, Type type) {
        return getDAO(type).read(id);
    }

    @Override
    public <T extends DataSet> List<T> readAll(Type type) {
        return getDAO(type).readAll();
    }

    @Override
    public void delete(DataSet dataSet) {
        Type type = DataSet.class;
        getDAO(type).delete(dataSet);
    }

    @Override
    public void shutdown() {
        myORMExecutor.close();
        myORMExecutor.cacheDispose();
        log.info("dbService stopped");
    }

    private BaseDAO getDAO(Type type) {
        return daos.get(type);
    }
}
