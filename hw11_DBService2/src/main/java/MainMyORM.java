import db.AddressDataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import dbService.DBService;
import dbService.DBServiceMyORMImpl;
import myorm.MyORMConfig;

import java.util.ArrayList;
import java.util.List;

public class MainMyORM {
    public static void main(String[] args) {
        String status;

        MyORMConfig config = new MyORMConfig();
        config.setParameter("url","jdbc:h2:tcp://localhost/~/test");
        config.setParameter("username","sa");
        config.setParameter("password","");
        config.setParameter("cache_max_elements","25");
        config.setParameter("cache_life_time_ms","0");
        config.setParameter("cache_idle_time_ms","0");
        config.setParameter("cache_is_eternal","true");

        DBService dbService = new DBServiceMyORMImpl(config);
        status = dbService.getLocalStatus();
        System.out.println(status);

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
        for (int i = 0; i < 15; ++i) {
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

        dbService.shutdown();
    }
}
