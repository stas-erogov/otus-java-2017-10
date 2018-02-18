package http;

import db.AddressDataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import dbService.DBService;
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
import java.util.ArrayList;
import java.util.List;

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

        someActions(dbService);
        PrintWriter out = resp.getWriter();
        out.println("DB Started: " + dbService.getLocalStatus());
    }

    private void someActions(DBService dbService) throws IOException {
        AddressDataSet address1 = new AddressDataSet("1", "address1");
        dbService.save(address1);

        UserDataSet user1 = new UserDataSet("user1", 10, address1);
        user1.addPhone(new PhoneDataSet("101"));
        user1.addPhone(new PhoneDataSet("102"));
        user1.addPhone(new PhoneDataSet("103"));
        dbService.save(user1);

        UserDataSet user10 = (UserDataSet) dbService.read(user1.getId(), UserDataSet.class);
        log.info("Some action: " + user1.toString());
        log.info("Some action: " + user1.equals(user10));

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            AddressDataSet address = new AddressDataSet("000" + i, "address" + i);
            dbService.save(address);

            UserDataSet user = new UserDataSet("user_" + i, 10, address);
            user.addPhone(new PhoneDataSet("34300" + i));
            user.addPhone(new PhoneDataSet("49500" + i));
            dbService.save(user);
            ids.add(user.getId());
        }

        for (Long id : ids) {
            log.info(dbService.read(id, UserDataSet.class).toString());
        }
    }
}
