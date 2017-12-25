package ru.otus.L41;

public interface BenchmarkMBean {
    int getSize();
    void setSize(int size);

    long getSleep();
    void setSleep(long sleep);

    int getStep();
    void setStep(int step);
}
