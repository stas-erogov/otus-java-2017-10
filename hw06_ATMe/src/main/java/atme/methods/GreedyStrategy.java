package atme.methods;

import atme.Cassete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GreedyStrategy implements MethodStrategy {

    private static Map<Cassete, Integer> getNotes(int requested, List<Cassete> cassetes) {
        Map<Cassete, Integer> order = new HashMap<>();

        List<Cassete> tmp = cassetes.stream()
                .sorted((c1,c2)-> Integer.compare(c2.getNominal(), c1.getNominal()))
                .collect(Collectors.toList());

        for (Cassete c : tmp) {
            int amount = requested / c.getNominal();
            if (amount > 0) {
                amount = amount > c.getAmount() ? c.getAmount() : amount;
                for (int i = 0; i < amount; ++i) {
                    order.merge(c, 1, Integer::sum);
                }
                requested -= amount * c.getNominal();
            }
            if (requested < 0) {
                System.out.println(order);
                throw new ArithmeticException();
            }
        }

        return order;
    }

    @Override
    public Map calculate(int requested, List cassetes) {
        return getNotes(requested, cassetes);
    }
}
