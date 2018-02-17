import java.util.function.Function;

public class Mignon<T, R> extends Thread {

    private T argument;
    private Function<T, R> function;
    private R result;

    Mignon(T argument, Function function) {
        this.argument = argument;
        this.function = function;
    }

    @Override
    public void run() {
        result = function.apply(argument);
    }

    public R getResult() {
        return result;
    }
}