import db.AddressDataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import dbService.DBService;
import dbService.DBServiceMyORMImpl;
import http.JettyServer;
import http.LoginServlet;
import http.AdminServlet;
import myorm.MyORMConfig;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainMyORM {
    public static void main(String[] args) throws Exception {
        String status;

        MyORMConfig config = new MyORMConfig();
        config.setParameter("url","jdbc:h2:tcp://localhost/~/test");
        config.setParameter("username","sa");
        config.setParameter("password","");
        config.setParameter("cache_max_elements","80");
        config.setParameter("cache_life_time_ms","0");
        config.setParameter("cache_idle_time_ms","0");
        config.setParameter("cache_is_eternal","true");
        config.setParameter("http_port", "8080");
        config.setParameter("http_resource_base","html");
        config.setParameter("http_template_path","/tmpl");
        config.setParameter("http_template_adminpage","admin.html");
        config.setParameter("http_template_loginpage","login.html");
        config.setParameter("http_refresh_period_adminpage", "5000");
        config.setParameter("superuser", "admin");
        config.setParameter("superuser_password", "c3p0");

        DBService dbService = new DBServiceMyORMImpl(config);
        status = dbService.getLocalStatus();
        System.out.println(status);

        JettyServer jetty = new JettyServer(config);
        ServletHolder holder = new ServletHolder(new AdminServlet(config, dbService.getCache()));
        jetty.addServlet(holder, "/admin");

        holder = new ServletHolder(new LoginServlet(config));
        jetty.addServlet(holder, "/login");
        jetty.run();

        AddressDataSet address1 = new AddressDataSet("1", "address1");
        dbService.save(address1);

        UserDataSet user1 = new UserDataSet("user1", 10, address1);
        user1.addPhone(new PhoneDataSet("101"));
        user1.addPhone(new PhoneDataSet("102"));
        user1.addPhone(new PhoneDataSet("103"));
        dbService.save(user1);

        UserDataSet user10 = (UserDataSet) dbService.read(user1.getId(), UserDataSet.class);
        System.out.println(user10);
        System.out.println(user1.equals(user10));

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            AddressDataSet address = new AddressDataSet("000"+i, "address" + i);
            dbService.save(address);

            UserDataSet user = new UserDataSet("user_" + i, 10, address);
            user.addPhone(new PhoneDataSet("34300"+i));
            user.addPhone(new PhoneDataSet("49500"+i));
            dbService.save(user);
            ids.add(user.getId());
        }

        for (Long id : ids) {
            System.out.println(dbService.read(id, UserDataSet.class));
        }


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (!(input = reader.readLine()).equals("stop")) {
            try {
                int id = Integer.parseInt(input);
                System.out.println(dbService.read(id, UserDataSet.class));
            } catch (NumberFormatException e) {
            }
        }

        jetty.stop();
        dbService.shutdown();
    }
}
