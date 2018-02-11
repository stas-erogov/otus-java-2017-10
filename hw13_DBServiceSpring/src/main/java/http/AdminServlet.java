package http;

import cache.CacheEngine;
import myorm.MyORMConfig;
import myorm.MyORMparam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet {

    @Autowired
    private MyORMConfig myORMConfig;

    @Autowired
    private CacheEngine cacheEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = (String) req.getSession().getAttribute("login");
        login = (login == null ? "" : login);

        String page;
        if (login.equals(myORMConfig.getParameter(MyORMparam.SUPERUSER))) {
            Map<String, Object> data = new HashMap<>();
            data.put("cacheHits", cacheEngine.getHitCount());
            data.put("cacheMiss", cacheEngine.getMissCount());
            data.put("refreshPeriod", myORMConfig.getParameter(MyORMparam.HTTP_REFRESH_PERIOD_ADMINPAGE));
            data.put("login", login);
            data.put("method", req.getMethod());
            data.put("URL", req.getRequestURL().toString());
            data.put("locale", req.getLocale());
            data.put("sessionId", req.getSession().getId());
            data.put("parameters", req.getParameterMap().toString());

            String templatePath = myORMConfig.getParameter(MyORMparam.HTTP_TEMPLATE_PATH);
            String template = myORMConfig.getParameter(MyORMparam.HTTP_TEMPLATE_ADMINPAGE);

            page = TemplateProcessor.getInstance().getPage(templatePath, template, data);
        } else {
            resp.setContentType("text/html");
            page = "Invalid credentials";
        }
        resp.getWriter().println(page);
    }
}
