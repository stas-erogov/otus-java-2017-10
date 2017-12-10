import atme.Cassete;
import atme.methods.GreedyStrategy;
import department.ATMDecorator;
import department.Department;
import department.Event;

@SuppressWarnings("WeakerAccess")
public class Main {

    public static void main(String[] args) {
        ATMDecorator atm = new ATMDecorator("ATM#1 init");
        atm.loadCassete(new Cassete(10, 10));
        atm.loadCassete(new Cassete(50, 10));
        atm.loadCassete(new Cassete(100, 10));

        ATMDecorator atm2 = new ATMDecorator(new GreedyStrategy(), "ATM#2 init");
        atm2.loadCassete(new Cassete(10, 20));
        atm2.loadCassete(new Cassete(50, 10));
        atm2.loadCassete(new Cassete(100, 50));

        Department dept = new Department();
        dept.addATM(atm);
        dept.addATM(atm2);
        checkStates(dept);

        System.out.println(atm.withdraw(1800));
        System.out.println(atm2.withdraw(120));

        dept.notify(Event.SHOW_BALANCE);
        System.out.println("Total balance: " + dept.getAllBalances());
        atm.setATMState("#1 on balance: " + atm.getBalance());
        atm2.setATMState("#2 on balance: " + atm2.getBalance());

        checkStates(dept);

        dept.notify(Event.RESTORE_STATE);
        checkStates(dept);
    }

    public static void checkStates(Department department) {
        System.out.println("check states: ");
        department.notify(Event.SHOW_STATE);
    }
}