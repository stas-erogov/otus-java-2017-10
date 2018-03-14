package messages;

import msgserver.Message;

public class LoginMessage extends Message {
    private String user;
    private String password;

    protected LoginMessage(String sender, String receiver) {
        super(sender, receiver);
    }

    @Override
    public void exec() {

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
}
