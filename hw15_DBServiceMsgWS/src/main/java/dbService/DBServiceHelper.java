package dbService;

import db.AddressDataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBServiceHelper {

    public static void someActions(DBService dbService) {
        Logger log = LoggerFactory.getLogger("Common");
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
