package myorm;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHelperH2 {
    public static final String STATUS_QUERY = "select h2version() from dual";

    public static Connection getConnection(String url, String username, String password) {
        Driver driver = new org.h2.Driver();

        try {
            DriverManager.registerDriver(driver);

            Properties properties = new Properties();
            properties.put("user", username);
            properties.put("password", password);
            return DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }
}
