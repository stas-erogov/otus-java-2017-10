package myorm;

import java.util.HashMap;
import java.util.Map;

public enum MyORMparam {
    URL("url"),
    USERNAME("username"),
    PASSWORD("password"),
    CACHE_MAX_ELEMENTS("cache_max_elements"),
    CACHE_LIFE_TIME_MS("cache_life_time_ms"),
    CACHE_IDLE_TIME_MS("cache_idle_time_ms"),
    CACHE_IS_ETERNAL("cache_is_eternal");

    private String name;
    private static Map<String, MyORMparam> paramsMap;

    @Override
    public String toString() {
        return this.name;
    }

    MyORMparam(String name) {
        this.name = name;
    }

    private static void initialize() {
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
            MyORMparam[] params = MyORMparam.values();
            for(MyORMparam name : params) {
                paramsMap.put(name.toString(), name);
            }
        }
    }

    public static MyORMparam getMyORMparam(String name) {
        initialize();
        return paramsMap.get(name);
    }
}
