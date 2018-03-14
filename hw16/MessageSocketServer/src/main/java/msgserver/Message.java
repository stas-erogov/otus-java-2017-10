package msgserver;

import worker.MessageWorker;

public abstract class Message {
    public static final String CLASS_NAME_VARIABLE="type";
    private String sender;
    private String receiver;
    private final String type;

    private MessageSystemContext context;
    private MessageWorker messageWorker;

    protected Message(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
        type = this.getClass().getName();
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public MessageWorker getMessageWorker() {
        return messageWorker;
    }

    public void setMessageWorker(MessageWorker messageWorker) {
        this.messageWorker = messageWorker;
    }
}
