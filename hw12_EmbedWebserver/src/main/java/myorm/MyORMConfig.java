package myorm;

import java.util.HashMap;
import java.util.Map;

public class MyORMConfig {
    private final Map<MyORMparam, String> parameters;

    public MyORMConfig() {
        this.parameters = new HashMap<>();
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
}