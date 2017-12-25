import atme.ATM;
import atme.Cassete;
import atme.methods.GreedyStrategy;

@SuppressWarnings("WeakerAccess")
public class Main {

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.loadCassete(new Cassete(10, 10));
        atm.loadCassete(new Cassete(60, 10));
        atm.loadCassete(new Cassete(100, 10));

        System.out.println("Start balance: " + atm.getBalance());
        System.out.println(atm.withdraw(120));
        System.out.println("End balance: " + atm.getBalance());

        ATM atm2 = new ATM(new GreedyStrategy());
        atm2.loadCassete(new Cassete(10, 100));
        atm2.loadCassete(new Cassete(60, 1));
        atm2.loadCassete(new Cassete(100, 100));

        System.out.println("Start balance: " + atm2.getBalance());
        System.out.println(atm2.withdraw(120));
        System.out.println("End balance: " + atm2.getBalance());
    }
}