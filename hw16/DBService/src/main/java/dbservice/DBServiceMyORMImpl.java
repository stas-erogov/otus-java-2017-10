package dbservice;

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
        if (message instanceof LoginMessage) {
            logger.info("DBService receive login request message");

            LoginMessage loginMessage = (LoginMessage) message;
            boolean isAdmin = isAdmin(loginMessage);
            logger.info("isAdmin: " + isAdmin);

            worker.accept(new CredentialAnswerMessage(worker.getSubscriber(), message.getSender(), isAdmin));
        }

        if (message instanceof CacheQueryMessage) {
            logger.info("DBService receive CacheQueryMessage");

            int hits = myORMExecutor.getCacheEngine().getHitCount();
            int missed = myORMExecutor.getCacheEngine().getMissCount();
            logger.info("Cache status. Hits: " + hits + ", missed: "+ missed);

            worker.accept(new CacheStatusAnswerMessage(worker.getSubscriber(), message.getSender(), hits, missed));
        }

        if (message instanceof SomeActionsMessage) {
            DBServiceHelper.someActions(this);
        }

        if (message instanceof HandshakeMessage) {
            worker.accept(new HandshakeMessage(this.worker.getSubscriber(), ""));
        }
    }

    private boolean isAdmin(LoginMessage loginMessage) {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (FileNotFoundException e) {
            logger.error("File not found front.properties", e);
        } catch (IOException e) {
            logger.error("Loading properties error", e);
        }
        String login = properties.getProperty("admin.login");
        String password = properties.getProperty("admin.password");
        return login.equals(loginMessage.getUser()) && password.equals(loginMessage.getPassword());
    }
}
