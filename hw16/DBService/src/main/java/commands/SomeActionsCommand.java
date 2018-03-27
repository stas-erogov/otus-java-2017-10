package commands;

import dbservice.DBService;
import dbservice.DBServiceHelper;
import msgserver.Message;
import worker.MessageWorker;

public class SomeActionsCommand extends Command {
    private DBService dbService;
    public SomeActionsCommand(MessageWorker worker, DBService dbService) {
        super(worker);
        this.dbService = dbService;
    }

    @Override
    public void execute(Message message) {
        DBServiceHelper.someActions(dbService);
    }
}
