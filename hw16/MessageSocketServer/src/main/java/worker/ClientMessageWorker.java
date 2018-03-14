package worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class ClientMessageWorker extends MessageWorker {
    private static final Logger logger = LoggerFactory.getLogger(ClientMessageWorker.class);

    private final Socket socket;

    public ClientMessageWorker(String host, int port, String subscriber) throws IOException {
        this(new Socket(host, port));
        this.setSubscriber(subscriber);
    }

    private ClientMessageWorker(Socket socket) {
        super(socket);
        this.socket = socket;
    }

    public void close() throws IOException {
        super.close();
        socket.close();
    }
}
