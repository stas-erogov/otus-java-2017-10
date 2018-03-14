package frontend;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wsserver.FrontendServlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

public class Frontend {
    private static Logger logger = LoggerFactory.getLogger(Frontend.class);

    public static void main(String[] args) throws Exception {
        int port = getPort(args);

        URL url = Frontend.class.getClassLoader().getResource("webapp/");
        URI uri = url != null ? url.toURI() : null;

        logger.info("URI = " + uri);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setBaseResource(Resource.newResource(uri));
        context.setWelcomeFiles(new String[]{"index.html"});
        context.addServlet(new ServletHolder("default", DefaultServlet.class), "/");

        context.addServlet(new ServletHolder(new FrontendServlet()), "/ws");

        Server server = new Server(port);
        server.setHandler(context);
        server.start();
        server.join();
    }

    private static int getPort(String[] args) {
        int port;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                return port;
            } catch (NumberFormatException e) {
            }
        }

        Properties properties = new Properties();
        try {
            properties.load(Frontend.class.getClassLoader().getResourceAsStream("front.properties"));
        } catch (FileNotFoundException e) {
            logger.error("File not found front.properties", e);
        } catch (IOException e) {
            logger.error("Loading properties error", e);
        }
        port = Integer.parseInt(properties.getProperty("http.port"));
        return port;
    }
}
