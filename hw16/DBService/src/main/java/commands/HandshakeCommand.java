package commands;

import messages.HandshakeMessage;
import msgserver.Message;
import worker.MessageWorker;

public class HandshakeCommand extends Command {
    public HandshakeCommand(MessageWorker worker) {
        super(worker);
    }

    @Override
    public void execute(Message message) {
        String sender = this.getWorker().getSubscriber();
        this.getWorker().accept(new HandshakeMessage(sender, ""));
    }
}
