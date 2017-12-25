package atme;

import atme.methods.MethodStrategy;
import atme.methods.MinNotesStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ATM {
    private final List<Cassete> cassetes = new ArrayList<>();
    private final MethodStrategy methodStrategy;

    public ATM (){
        this.methodStrategy = new MinNotesStrategy();
    }

    public ATM (MethodStrategy methodStrategy) {
        this.methodStrategy = methodStrategy;
    }

    public void loadCassete (Cassete cassete) {
        cassetes.add(cassete);
    }

    public boolean withdraw (int requested) {
        Map<Cassete, Integer> order = methodStrategy.calculate(requested, cassetes);
        boolean result = false;

        for (Map.Entry<Cassete, Integer> e : order.entrySet()) {
            result = e.getKey().withdraw(e.getValue());
            if (!result) {
                break;
            }
        }

        return result;
    }

    public int getBalance() {
        return cassetes.stream().mapToInt(c -> c.getNominal() * c.getAmount()).sum();
    }
}