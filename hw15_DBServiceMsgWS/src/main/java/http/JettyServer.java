package http;

import myorm.MyORMConfig;
import myorm.MyORMparam;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class JettyServer {
    private Server jetty;
    private ResourceHandler resourceHandler;
    private ServletContextHandler contextHandler;

    public void addServlet(ServletHolder holder, String path) {
        contextHandler.addServlet(holder, path);
    }

    public void addServlet(Class<? extends Servlet> clazz, String path) {
        contextHandler.addServlet(clazz, path);
    }

    public void run() throws Exception {
        jetty.setHandler(new HandlerList(resourceHandler, contextHandler));
        jetty.start();
    }

    public JettyServer (MyORMConfig myORMConfig) {
        int http_port = Integer.parseInt(myORMConfig.getParameter(MyORMparam.HTTP_PORT));
        jetty = new Server(http_port);

        resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(myORMConfig.getParameter(MyORMparam.HTTP_RESOURCE_BASE));
        contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    }

    public void stop() throws Exception {
        jetty.stop();
    }
}
