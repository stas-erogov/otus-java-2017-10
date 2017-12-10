package department;

import atme.ATM;
import atme.methods.MethodStrategy;

public class ATMDecorator extends ATM implements ATMObserver{
    private String ATMState;

    private String getATMState() {
        return ATMState;
    }

    public void setATMState(String ATMState) {
        this.ATMState = ATMState;
    }

    public ATMDecorator(String ATMState) {
        this.ATMState = ATMState;
        storeState = new ATMMemento();
    }

    public ATMDecorator(MethodStrategy methodStrategy, String ATMState) {
        super(methodStrategy);
        this.ATMState = ATMState;
        storeState = new ATMMemento();
    }

    private ATMMemento storeState;

    @Override
    public void notify(Event event) {
        switch (event) {
            case RESTORE_STATE:
                this.restoreState();
                break;
            case SHOW_STATE:
                System.out.println(this.getATMState());
                break;
            case SHOW_BALANCE:
                System.out.println(this.getBalance());
                break;
        }
    }

    private class ATMMemento {
        final String someState;

        public String getState() {
            return someState;
        }

        public ATMMemento() {
            this.someState = ATMState;
        }
    }

    private void restoreState() {
        this.ATMState = storeState.getState();
    }
}
