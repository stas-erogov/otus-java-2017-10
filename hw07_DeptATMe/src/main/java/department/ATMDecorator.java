package department;

import atme.ATM;
import atme.Cassete;

import java.util.ArrayList;
import java.util.List;

public class ATMDecorator implements ATM, ATMObserver {
    private final ATM atm;
    private final List<MemoCassete> memCassetes = new ArrayList<>();

    public ATMDecorator(ATM atm) {
        this.atm = atm;
    }

    public void loadCassete(MemoCassete cassete) {
        memCassetes.add(cassete);
        atm.loadCassete(cassete);
    }

    @Override
    public void loadCassete(Cassete cassete) {
        atm.loadCassete(cassete);
    }

    @Override
    public boolean withdraw(int requested) {
        return atm.withdraw(requested);
    }

    @Override
    public int getBalance() {
        return atm.getBalance();
    }

    @Override
    public void notify(Event event) {

        switch (event) {
            case RESTORE_STATE:
                restoreState();
                break;
            case SHOW_STATE:
                break;
            case SHOW_BALANCE:
                System.out.println(this.getBalance());
                break;
        }

    }

    private void restoreState() {
        memCassetes.stream()
                .forEach(c->c.restoreState());
    }
}
