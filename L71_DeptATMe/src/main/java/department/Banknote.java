package department;

import java.util.Arrays;

public enum Banknote {
    ONE(1),
    FIVE(5),
    TEN(10),
    FIFTY(50),
    ONE_HUNDRED(100);

    private int _nominal;

    Banknote(int nominal) {
        _nominal = nominal;
    }

    public int getNominal() {
        return _nominal;
    }

    public static Banknote getBanknote(int value) {
        for (Banknote banknote : Banknote.values()) {
            if (banknote.getNominal() == value){
                return banknote;
            }
        }
        return null;
    }
}
