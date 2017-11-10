package ru.otus.L41;

import java.util.ArrayList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {
    private volatile int size = 100;
    private volatile long sleep = 500;
    private volatile int step = 1000;

    private static final List list = new ArrayList();

    void run() throws InterruptedException {
        int i = 0;

        while (true) {
            list.add(new char[size]);
            i++;
            if (i % step == 0) {
                for (int j = step - 1; j > (step - 1)/2; j--) {
                    list.remove(j);
                }
                Thread.sleep(sleep);
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public long getSleep() {
        return sleep;
    }

    @Override
    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    @Override
    public int getStep() {
        return step;
    }

    @Override
    public void setStep(int step) {
        this.step = step;
    }
}
