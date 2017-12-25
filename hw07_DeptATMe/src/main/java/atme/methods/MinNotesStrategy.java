package atme.methods;

import atme.Cassete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinNotesStrategy implements MethodStrategy {
    private static final int INFINITE = Integer.MAX_VALUE;

    private static int[] getMinNotes (int requested_cash, List<Cassete> cassetes) {
        int minNotes[] = new int[requested_cash+1];
        minNotes[0] = 0;

        for (int m = 1; m <= requested_cash; ++m) {
            minNotes[m] = INFINITE;
            for (Cassete c : cassetes) {
                if (m >= c.getNominal() && minNotes[m - c.getNominal()] + 1 < minNotes[m]) {
                    minNotes[m] = minNotes[m - c.getNominal()] + 1;
                }
            }
        }
        return minNotes;
    }

    private static Map<Cassete, Integer> getNotes(int requested, List<Cassete> cassetes) {
        Map<Cassete, Integer> order = new HashMap<>();

        int minNotes[] = getMinNotes(requested, cassetes);

        if (minNotes[requested] != INFINITE) {
            while (requested > 0) {
                for (Cassete c : cassetes) {
                    if (minNotes[requested - c.getNominal()] == minNotes[requested] - 1) {
                        order.merge(c, 1, Integer::sum);
                        requested -= c.getNominal();
                        break;
                    }
                }
            }
        }
        return order;
    }

    @Override
    public Map calculate(int requested, List cassetes) {
        return getNotes(requested, cassetes);
    }
}
