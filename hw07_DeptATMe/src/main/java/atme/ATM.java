package atme;

public interface ATM {
    void loadCassete(Cassete cassete);

    boolean withdraw(int requested);

    int getBalance();
}
