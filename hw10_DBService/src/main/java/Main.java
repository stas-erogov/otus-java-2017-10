import db.AddressDataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import dbService.DBService;
import dbService.DBServiceHibernateImpl;

import java.util.List;

public class Main {
    public static void main(String...args){
        DBService dbService = new DBServiceHibernateImpl();

        String status = dbService.getLocalStatus();
        System.out.println("Status: " + status);

        UserDataSet user1 = new UserDataSet("user1", new AddressDataSet("000001", "address1"));
        AddressDataSet address2 = new AddressDataSet("000002", "address2");

        dbService.save(user1);
        dbService.save(address2);

        UserDataSet user2 = new UserDataSet("user2", address2);
        dbService.save(user2);

        UserDataSet user3 = new UserDataSet("user3",
                new AddressDataSet("000003", "address3"),
                new PhoneDataSet("3430000001"));
        user3.addPhone(new PhoneDataSet("3430000002"));
        dbService.save(user3);

        PhoneDataSet phoneDataSet2 = new PhoneDataSet("4950000001");
        user1.addPhone(phoneDataSet2);
        user1.addPhone(new PhoneDataSet("4950000002"));
        dbService.save(user1);

        List<UserDataSet> userList = dbService.readAll(UserDataSet.class);
        userList.forEach(System.out::println);

        user1.setAge(1);
        dbService.save(user1);

        UserDataSet dataSet = (UserDataSet) dbService.read(1, UserDataSet.class);
        System.out.println(dataSet);

        dbService.delete(user2);

        userList = dbService.readAll(UserDataSet.class);
        userList.forEach(System.out::println);

        dbService.shutdown();
    }
}
