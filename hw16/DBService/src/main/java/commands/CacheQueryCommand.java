package commands;

import messages.CacheStatusAnswerMessage;
import msgserver.Message;
import myorm.MyORMExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.MessageWorker;

public class CacheQueryCommand extends Command {
    private Logger logger = LoggerFactory.getLogger(CacheQueryCommand.class);

    private MyORMExecutor myORMExecutor;
    public CacheQueryCommand(MessageWorker worker, MyORMExecutor myORMExecutor) {
        super(worker);
        this.myORMExecutor = myORMExecutor;
    }

    @Override
    public void execute(Message message) {
        logger.info("DBService receive CacheQueryMessage");

        int hits = myORMExecutor.getCacheEngine().getHitCount();
        int missed = myORMExecutor.getCacheEngine().getMissCount();
        logger.info("Cache status. Hits: " + hits + ", missed: "+ missed);

        String sender = this.getWorker().getSubscriber();
        String receiver = message.getSender();
        this.getWorker().accept(new CacheStatusAnswerMessage(sender, receiver, hits, missed));
    }
}
