package messages;

import msgserver.Message;

public class HandshakeMessage extends Message {
    public HandshakeMessage(String sender, String receiver) {
        super(sender, receiver);
    }

    @Override
    public void exec() {

    }
}
