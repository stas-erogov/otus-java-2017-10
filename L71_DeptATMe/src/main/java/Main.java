import atme.ATMBase;
import department.*;

@SuppressWarnings("WeakerAccess")
public class Main {

    public static void main(String[] args) {
        ATMDecorator atm = new ATMDecorator(new ATMBase());
        atm.loadCassete(new MemoCassete(Banknote.TEN, 10));
        atm.loadCassete(new MemoCassete(Banknote.FIFTY, 10));

        Department dept = new Department();
        dept.addATM(atm);

        System.out.println("Before:");
        dept.notify(Event.SHOW_BALANCE);

        atm.withdraw(300);
        System.out.println("After withdraw:");
        dept.notify(Event.SHOW_BALANCE);


        dept.notify(Event.RESTORE_STATE);
        System.out.println("After restore:");
        dept.notify(Event.SHOW_BALANCE); // <--- Должно быть 600
    }
}