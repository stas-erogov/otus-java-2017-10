package messages;

import msgserver.Message;

public class CredentialAnswerMessage extends Message {
    private boolean isAdmin;
    public CredentialAnswerMessage(String sender, String receiver, boolean isAdmin) {
        super(sender, receiver);
        this.isAdmin = isAdmin;
    }

    @Override
    public void exec() {

    }
}
