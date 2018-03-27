package commands;

import main.Main;
import messages.CredentialAnswerMessage;
import messages.LoginMessage;
import msgserver.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.MessageWorker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LoginCommand extends Command {
    private Logger logger = LoggerFactory.getLogger(LoginCommand.class);

    public LoginCommand(MessageWorker worker) {
        super(worker);
    }

    @Override
    public void execute(Message message) {
        logger.info("DBService receive login request message");

        LoginMessage loginMessage = (LoginMessage) message;
        boolean isAdmin = isAdmin(loginMessage);
        logger.info("isAdmin: " + isAdmin);

        this.getWorker().accept(new CredentialAnswerMessage(this.getWorker().getSubscriber(), message.getSender(), isAdmin));
    }

    private boolean isAdmin(LoginMessage loginMessage) {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (FileNotFoundException e) {
            logger.error("File not found front.properties", e);
        } catch (IOException e) {
            logger.error("Loading properties error", e);
        }
        String login = properties.getProperty("admin.login");
        String password = properties.getProperty("admin.password");
        return login.equals(loginMessage.getUser()) && password.equals(loginMessage.getPassword());
    }
}
