package http;

import cache.CacheService;
import myorm.MyORMConfig;
import myorm.MyORMparam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet {
    private MyORMConfig myORMConfig;
    private CacheService cache;

    public AdminServlet(MyORMConfig myORMConfig, CacheService cache) {
        this.myORMConfig = myORMConfig;
        this.cache = cache;
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
            data.put("cacheHits", cache.getHitCount());
            data.put("cacheMiss", cache.getMissCount());
            data.put("refreshPeriod", myORMConfig.getParameter(MyORMparam.HTTP_REFRESH_PERIOD_ADMINPAGE));
            data.put("login", login);
            data.put("method", req.getMethod());
            data.put("URL", req.getRequestURL().toString());
            data.put("locale", req.getLocale());
            data.put("sessionId", req.getSession().getId());
            data.put("parameters", req.getParameterMap().toString());

            String path = myORMConfig.getParameter(MyORMparam.HTTP_TEMPLATE_PATH);
            String template = myORMConfig.getParameter(MyORMparam.HTTP_TEMPLATE_ADMINPAGE);

            page = TemplateProcessor.getInstance().getPage(path + File.separator + template, data);
        } else {
            resp.setContentType("text/html");
            page = "Invalid credentials";
        }
        resp.getWriter().println(page);
    }
}
