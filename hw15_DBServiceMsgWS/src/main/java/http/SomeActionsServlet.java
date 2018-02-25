package http;

import dbService.DBService;
import dbService.DBServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SomeActionsServlet extends HttpServlet {
    Logger log = LoggerFactory.getLogger("Common");

    @Autowired
    private DBService dbService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, servletConfig.getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (dbService == null) {
            log.error("doPost: dbService is null");
        }
        log.info("doPost: " + dbService.getLocalStatus());

        DBServiceHelper.someActions(dbService);
        PrintWriter out = resp.getWriter();
        out.println("DB Started: " + dbService.getLocalStatus());
    }
}
