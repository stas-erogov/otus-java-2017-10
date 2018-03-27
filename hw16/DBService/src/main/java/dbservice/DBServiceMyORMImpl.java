package dbservice;

import commands.*;
import dao.AddressesDAOMyORM;
import dao.BaseDAO;
import dao.PhonesDAOMyORM;
import dao.UsersDAOMyORM;
import db.AddressDataSet;
import db.DataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import main.Main;
import messages.*;
import msgserver.EndPointService;
import msgserver.Message;
import myorm.MyORMExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.MessageWorker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class DBServiceMyORMImpl implements DBService, EndPointService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<Type, BaseDAO> daos = new HashMap<>();
    private MyORMExecutor myORMExecutor;

    private MessageWorker worker;

    private Map<Class<? extends Message>, Command> commandsRegistry = new HashMap<>();

    public DBServiceMyORMImpl(MyORMExecutor myORMExecutor) {
        this.myORMExecutor = myORMExecutor;
    }

    public void setWorker(MessageWorker worker) {
        this.worker = worker;
    }

    public void init() {
        daos.put(UserDataSet.class, new UsersDAOMyORM(myORMExecutor));
        daos.put(AddressDataSet.class, new AddressesDAOMyORM(myORMExecutor));
        daos.put(PhoneDataSet.class, new PhonesDAOMyORM(myORMExecutor));
        worker.setEndPointService(this);

        commandsRegistry.put(LoginMessage.class, new LoginCommand(worker));
        commandsRegistry.put(CacheQueryMessage.class, new CacheQueryCommand(worker, myORMExecutor));
        commandsRegistry.put(SomeActionsMessage.class, new SomeActionsCommand(worker, this));
        commandsRegistry.put(HandshakeMessage.class, new HandshakeCommand(worker));
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
        logger.info("dbService stopped");
    }

    private BaseDAO getDAO(Type type) {
        return daos.get(type);
    }

    @Override
    public void execute(Message message) {
        commandsRegistry.get(message.getClass()).execute(message);
    }
}
