package http;

import dbService.DBService;
import dbService.DBServiceMyORMImpl;
import myorm.MyORMConfig;
import myorm.MyORMparam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private String user;
    private String password;

    @Autowired
    private DBService dbService;

    @Autowired
    private MyORMConfig myORMConfig;

    public void setDbService(DBService dbService) {
        this.dbService = dbService;
    }

    public void setMyORMConfig(MyORMConfig myORMConfig) {
        this.myORMConfig = myORMConfig;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.myORMConfig = ((DBServiceMyORMImpl) dbService).getMyORMConfig();

        String templatePath = myORMConfig.getParameter(MyORMparam.HTTP_TEMPLATE_PATH);
        String template = myORMConfig.getParameter(MyORMparam.HTTP_TEMPLATE_LOGINPAGE);
        this.user = myORMConfig.getParameter(MyORMparam.SUPERUSER);
        this.password = myORMConfig.getParameter(MyORMparam.SUPERUSER_PASSWORD);

        String page = TemplateProcessor.getInstance().getPage(templatePath, template, null);
        resp.getWriter().println(page);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqUser = req.getParameter("user");
        String reqPasswd = req.getParameter("pwd");

        req.getSession().setAttribute("login", reqUser);

        if (reqUser.equals(user) && reqPasswd.equals(password)) {
            req.getRequestDispatcher("/admin").forward(req, resp);
        } else {
            req.getRequestDispatcher("/admin").include(req, resp);
            resp.sendRedirect("index.html");
        }
    }
}
