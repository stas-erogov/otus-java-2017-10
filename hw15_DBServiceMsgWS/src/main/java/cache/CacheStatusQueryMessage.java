package cache;

import msgserver.Message;
import wsserver.WebSocketHandler;

public class CacheStatusQueryMessage extends Message {
    protected CacheStatusQueryMessage(String sender, String receiver) {
        super(sender, receiver);
    }

    @Override
    public void exec() {
        CacheEngine cacheEngine = (CacheEngine) getContext().getSubscriber(getReceiver());
        int hits = cacheEngine.getHitCount();
        int missed = cacheEngine.getMissCount();
        Message message = new CacheAnswerMessage(getReceiver(), getSender(), hits, missed);
        message.setContext(getContext());
        getContext().getMessageSystem().sendMessage(message);
    }

    private class CacheAnswerMessage extends Message {
        private int hits;
        private int missed;

        protected CacheAnswerMessage(String sender, String receiver, int hits, int missed) {
            super(sender, receiver);
            this.hits = hits;
            this.missed = missed;
        }

        @Override
        public void exec() {
            WebSocketHandler front = (WebSocketHandler)getContext().getSubscriber(getReceiver());
            front.send("{\"type\":\"cache\",\"hits\":\""+ hits + "\",\"missed\":\""+ missed +"\"}");
        }
    }
}
