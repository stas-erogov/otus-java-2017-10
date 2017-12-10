package atme;

public class Cassete {
    private final int nominal;
    private int amount;

    public Cassete(int nominal, int amount) {
        this.nominal = nominal;
        this.amount = amount;
    }

    public int getNominal() {
        return nominal;
    }

    public int getAmount() {
        return amount;
    }

    public boolean withdraw (int requested) {
        if (requested <= amount) {
            amount -= requested;
            return true;
        }
        return false;
    }
}