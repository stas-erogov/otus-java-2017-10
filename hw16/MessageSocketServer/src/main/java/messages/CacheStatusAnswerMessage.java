package messages;

import msgserver.Message;

public class CacheStatusAnswerMessage extends Message {

    private int hits;

    private int missed;
    public CacheStatusAnswerMessage(String sender, String receiver, int hits, int missed) {
        super(sender, receiver);
        this.hits = hits;
        this.missed = missed;
    }

    @Override
    public void exec() {

    }
}
