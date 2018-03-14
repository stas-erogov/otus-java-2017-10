package messages;

import msgserver.Message;

public class SomeActionsMessage extends Message {
    protected SomeActionsMessage(String sender, String receiver) {
        super(sender, receiver);
    }

    @Override
    public void exec() {

    }
}
