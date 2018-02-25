package dbService;

import msgserver.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SomeActionMessage extends Message {
    private static final Logger logger = LoggerFactory.getLogger("Common");
    protected SomeActionMessage(String sender, String receiver) {
        super(sender, receiver);
    }

    @Override
    public void exec() {
        DBService dbService = (DBServiceMyORMImpl) getContext().getSubscriber(getReceiver());
        DBServiceHelper.someActions(dbService);
        logger.info("Some business actions was performed");
    }
}
