package msgserver;

public abstract class Message {
    private final String sender;
    private final String receiver;

    private MessageSystemContext context;

    protected Message(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public String getSender() {
        return this.sender;
    }

    public MessageSystemContext getContext() {
        return context;
    }

    public void setContext(MessageSystemContext context) {
        this.context = context;
    }

    public abstract void exec();

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", context=" + context +
                '}';
    }

}
