package commands;

import msgserver.Message;
import worker.MessageWorker;

public abstract class Command {
    private MessageWorker worker;
    public Command(MessageWorker worker) {
        this.worker = worker;
    }
    public abstract void execute(Message message);

    public MessageWorker getWorker() {
        return worker;
    }
}
