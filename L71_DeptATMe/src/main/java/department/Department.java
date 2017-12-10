package department;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final List<ATMDecorator> atms = new ArrayList<>();

    public void addATM (ATMDecorator atm) {
        atms.add(atm);
    }

    public int getAllBalances() {
        return atms.stream()
                .mapToInt(e->e.getBalance())
                .sum();
    }

    public void notify(Event event) {
        atms.forEach(o -> o.notify(event));
    }

}
