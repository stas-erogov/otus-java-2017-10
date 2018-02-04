package http;

import myorm.MyORMConfig;
import myorm.MyORMparam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final String user;
    private final String password;
    private String template_path;
    private String login_page;

    public LoginServlet(MyORMConfig myORMConfig) {
        this.template_path = myORMConfig.getParameter(MyORMparam.HTTP_TEMPLATE_PATH);
        this.login_page = myORMConfig.getParameter(MyORMparam.HTTP_TEMPLATE_LOGINPAGE);
        this.user = myORMConfig.getParameter(MyORMparam.SUPERUSER);
        this.password = myORMConfig.getParameter(MyORMparam.SUPERUSER_PASSWORD);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = TemplateProcessor.getInstance().getPage(template_path + File.separator + login_page, null);
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
