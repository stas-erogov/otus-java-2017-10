package core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import server.MessageSocketServer;

public class MessageServerApp {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(MessageServerConfig.class);
        MessageSocketServer messageSocketServer = ctx.getBean(MessageSocketServer.class);
        messageSocketServer.start();
    }
}
