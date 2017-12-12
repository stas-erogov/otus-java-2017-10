package department;

import atme.Cassete;

public class MemoCassete extends Cassete {

    public MemoCassete(Banknote nominal, int amount) {
        super(nominal.getNominal(), amount);
        restore = new Memento(amount);
    }

    private Memento restore;
    private class Memento {
        int memAmount;

        Memento (int memAmount) {
            this.memAmount = memAmount;
        }

        int getAmount() {
            return memAmount;
        }
    }

    void restoreState() {
        super.setAmount(restore.getAmount());
    }
}
