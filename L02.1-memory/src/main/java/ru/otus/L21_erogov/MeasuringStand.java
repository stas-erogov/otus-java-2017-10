package ru.otus.L21_erogov;

import java.util.function.Supplier;

public class MeasuringStand  {
    public MeasuringStand() {
    }

    static <T> long getSize(Supplier<T> supplier, int size) {

        long memory_before;
        long memory_after;

        System.gc();
        Object[] array = new Object[size];

        memory_before = getMemorySize();
        for (int j = 0; j < size; j++) {
            array[j] = supplier.get();
        }
        memory_after  = getMemorySize();
        array = null;
        System.gc();
        return (memory_after - memory_before) / size;
    }

    private static long getMemorySize() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}