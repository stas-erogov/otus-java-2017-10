package department;

public enum Banknote {
    ONE,
    FIVE,
    TEN,
    FIFTY,
    ONE_HUNDRED;

    public int getNominal() {
        switch (this) {
            case ONE:
                return 1;
            case FIVE:
                return 5;
            case TEN:
                return 10;
            case FIFTY:
                return 50;
            case ONE_HUNDRED:
                return 100;
        }
        return 0;
    }

    public static Banknote getBanknote(int value) {
        switch (value) {
            case 1:
                return Banknote.ONE;
            case 5:
                return Banknote.FIVE;
            case 10:
                return Banknote.TEN;
            case 50:
                return Banknote.FIFTY;
            case 100:
                return Banknote.ONE_HUNDRED;
        }
        return null;
    }
}
