package myorm;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHelperH2 {
    public static Connection getConnection() {
        Driver driver = new org.h2.Driver();

        try {
            DriverManager.registerDriver(driver);
            String url = "jdbc:h2:tcp://localhost/~/test";

            Properties properties = new Properties();
            properties.put("user", "sa");
            properties.put("password", "");
            return DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static final String STATUS_QUERY = "select h2version() from dual";
}
