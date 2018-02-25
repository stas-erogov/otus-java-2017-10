package myorm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MyORMConfig {
    private final Map<MyORMparam, String> parameters;

    public MyORMConfig() {
        this.parameters = new HashMap<>();
    }

    public MyORMConfig(String filename) {
        this.parameters = new HashMap<>();
        this.loadFromFile(filename);
    }

    public void setParameter(String name, String value) {
        MyORMparam parameter;
        parameter = MyORMparam.getMyORMparam(name);
        if (parameter!=null) {
            parameters.put(parameter, value);
        }
    }

    public String getParameter(MyORMparam name) {
        return parameters.get(name);
    }

    private void loadFromFile(String filename) {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            properties.load(inputStream);

            Enumeration<?> e = properties.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);
                this.setParameter(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}