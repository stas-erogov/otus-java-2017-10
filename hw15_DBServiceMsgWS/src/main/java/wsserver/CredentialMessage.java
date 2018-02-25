package wsserver;

import msgserver.Message;
import msgserver.MessageSystem;
import myorm.MyORMConfig;
import myorm.MyORMparam;

public class CredentialMessage extends Message {
    private String user;
    private String password;

    protected CredentialMessage(String sender, String receiver, String user, String password) {
        super(sender, receiver);
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void exec() {
        boolean isAdmin;

        MyORMConfig config = (MyORMConfig) getContext().getSubscriber(super.getReceiver());
        String appUser = config.getParameter(MyORMparam.SUPERUSER);
        String appPassword = config.getParameter(MyORMparam.SUPERUSER_PASSWORD);

        MessageSystem messageSystem = getContext().getMessageSystem();

        isAdmin = appUser.equals(user) && appPassword.equals(password);
        Message message = new CredentialAnswer(getReceiver(), getSender(), isAdmin);
        message.setContext(getContext());
        messageSystem.sendMessage(message);
    }

    @Override
    public String toString() {
        return "CredentialMessage{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                "} " + super.toString();
    }

    private class CredentialAnswer extends Message {
        boolean isAdmin;
        CredentialAnswer(String sender, String receiver, boolean isAdmin) {
            super(sender, receiver);
            this.isAdmin = isAdmin;
        }

        @Override
        public void exec() {
            WebSocketHandler front = (WebSocketHandler)getContext().getSubscriber(getReceiver());
            front.send("{\"type\":\"credentials\",\"isAdmin\":\""+ isAdmin + "\"}");
        }
    }
}

