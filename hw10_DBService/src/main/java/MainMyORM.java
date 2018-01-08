import db.AddressDataSet;
import db.PhoneDataSet;
import db.UserDataSet;
import dbService.DBService;
import dbService.DBServiceMyORMImpl;

public class MainMyORM {
    public static void main(String[] args) {
        String status;

        DBService dbService = new DBServiceMyORMImpl();
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
    }
}
