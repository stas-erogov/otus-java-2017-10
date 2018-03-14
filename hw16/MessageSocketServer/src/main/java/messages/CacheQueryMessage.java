package messages;

import msgserver.Message;

public class CacheQueryMessage extends Message {
    protected CacheQueryMessage(String sender, String receiver) {
        super(sender, receiver);
    }

    @Override
    public void exec() {

    }
}
