package atme;

import atme.methods.MethodStrategy;
import atme.methods.MinNotesStrategy;
import department.Banknote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ATMBase implements ATM {
    private final List<Cassete> cassetes = new ArrayList<>();
    private final MethodStrategy methodStrategy;

    public ATMBase(){
        this.methodStrategy = new MinNotesStrategy();
    }

    public ATMBase(MethodStrategy methodStrategy) {
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
        if (result) {
            order.forEach((k,v)-> System.out.printf("issued %s banknotes of %s\n",
                    v.intValue(),
                    Banknote.getBanknote(k.getNominal())
                    )
            );
        }

        return result;
    }

    public int getBalance() {
        return cassetes.stream().mapToInt(c -> c.getNominal() * c.getAmount()).sum();
    }
}