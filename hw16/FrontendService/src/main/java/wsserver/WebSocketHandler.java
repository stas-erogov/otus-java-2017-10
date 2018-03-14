package wsserver;

import com.google.gson.Gson;
import messages.HandshakeMessage;
import worker.ClientMessageWorker;
import msgserver.EndPointService;
import msgserver.Message;
import msgserver.MessageUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.MessageWorker;

import java.io.IOException;

//set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_152
@WebSocket
public class WebSocketHandler implements EndPointService {
    private static final Logger logger = LoggerFactory.getLogger("Common");
    private Session session;
    private String sender;

    private MessageWorker worker;

    public WebSocketHandler() {
    }

    @OnWebSocketConnect
    public void onConnect(Session s) {
        logger.info("WebSocket connect: " + s.getRemoteAddress().toString() +
                ", protocol: " + s.getProtocolVersion() +
                ", session: " + s.toString());
        this.session = s;

        this.sender = "WebSocketSession" + String.valueOf(session.hashCode());
        try {
            worker = new ClientMessageWorker("localhost", 5050, sender);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("Client worker created " + worker.toString());
        worker.setEndPointService(this);
        worker.init();
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        logger.error("WebSocket error: " + t.getMessage());
    }

    @OnWebSocketClose
    public void onClose(int closeCode, String closeReason) {
        logger.info("WebSocket closed: " + closeReason);
    }

    @OnWebSocketMessage
    public void onMessage(String json) {
        logger.info("WebSocket onMessage: " + json);
        try {
            Message message = MessageUtils.getMessageFromJSON(json);
            message.setSender(sender);
            logger.info(message.toString());

            worker.accept(message);
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFound OnWebSocketMessage error: ", e);
        } catch (Exception e) {
            logger.error("OnWebSocketMessage error: ", e);
        }
    }

    public void send(String s) {
        logger.info("WebSocket sendback: " + s);
        try {
            session.getRemote().sendString(s);
        } catch (IOException e) {
            logger.error("WebSocket send error: ", e);
        }
    }

    @Override
    public void execute(Message message) {
        if (message instanceof HandshakeMessage) {
            worker.accept(new HandshakeMessage(this.sender, ""));
        } else {
            send(new Gson().toJson(message));
        }
    }
}
