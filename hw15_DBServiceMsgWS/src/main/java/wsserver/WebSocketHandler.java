package wsserver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import msgserver.Message;
import msgserver.MessageSystemContext;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
//set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_152
@WebSocket
public class WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger("Common");
    private Session session;
    private MessageSystemContext context;

    public WebSocketHandler(MessageSystemContext context) {
        this.context = context;
    }

    @OnWebSocketConnect
    public void onConnect(Session s) {
        logger.info("WebSocket connect: " + s.getRemoteAddress().toString() + ", protocol: " + s.getProtocolVersion());
        this.session = s;
        context.putContextSubscriber("websocketHandler", this);
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
            Message message = (Message) getMessageFromJson(json);

            logger.info(message.toString());
            message.setContext(context);

            context.getMessageSystem().sendMessage(message);
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFound OnWebSocketMessage error: ", e);
        } catch (Exception e) {
            logger.error("OnWebSocketMessage error: ", e);
        }
    }

    private Object getMessageFromJson(String json) throws ClassNotFoundException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String type = jsonObject.get("type").getAsString();
        String body = jsonObject.get("body").toString();
        logger.info("getMessageFromJson type: " + type);
        logger.info("getMessageFromJson body: " + body);

        Class<?> clazz = context.getMsgMapping().get(type);
        logger.info("getMessageFromJson clazz:" + clazz);

        return gson.fromJson(body, clazz);
    }

    public void send(String s) {
        logger.info("WebSocket sendback: " + s);
        try {
            session.getRemote().sendString(s);
        } catch (IOException e) {
            logger.error("WebSocket send error: ", e);
        }
    }
}
