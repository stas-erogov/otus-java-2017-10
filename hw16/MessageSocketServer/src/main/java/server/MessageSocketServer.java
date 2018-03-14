package server;

import msgserver.MessageSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.MessageWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageSocketServer extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(MessageSocketServer.class);

    private static final int PORT = 5050;
    private static final int MIRROR_DELAY_MS = 100;

    private MessageSystem messageSystem;

    private boolean running = true;

    public MessageSocketServer(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port " + serverSocket.getLocalPort());
            while (running) {
                Socket socket = serverSocket.accept();
                MessageWorker worker = new MessageWorker(socket);
                worker.setMessageSystem(messageSystem);
                worker.setEndPointService(messageSystem);
                worker.init();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
