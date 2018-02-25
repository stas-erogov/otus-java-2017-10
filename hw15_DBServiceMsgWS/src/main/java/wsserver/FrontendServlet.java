package wsserver;

import msgserver.MessageSystemContext;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class FrontendServlet extends WebSocketServlet {
    private static final String SUBSCRIBER_NAME = "wsHandler";
    private Logger logger = LoggerFactory.getLogger("Common");

    @Autowired
    private MessageSystemContext messageSystemContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.setCreator(new FrontCreator());
    }

    private class FrontCreator implements WebSocketCreator {
        @Override
        public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
            WebSocketHandler front = new WebSocketHandler(messageSystemContext);
            logger.info("WebSocketHandler created: " + front.toString());

            messageSystemContext.putContextSubscriber(SUBSCRIBER_NAME, front);
            logger.info("Subscriber WebSocketHandler registered in MessageSystemContext: " + messageSystemContext.getSubscriber(SUBSCRIBER_NAME));

            return front;
        }
    }
}
