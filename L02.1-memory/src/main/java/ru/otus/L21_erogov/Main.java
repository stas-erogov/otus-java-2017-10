package ru.otus.L21_erogov;

import java.lang.management.ManagementFactory;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        System.out.println("pid: " + ManagementFactory.getRuntimeMXBean().getName());

        int size = 25_000_000;
        System.out.println("Strings (40 bytes): ");
        printSize("Empty string size (500 000 elements): ", () -> new String(), 500_000);
        printSize("Empty string size: ", () -> new String(), size);
        printSize("String size: ", () -> new String("Arabica!"), size);

        System.out.println("Objects (8 bytes): ");
        printSize("Object size (500 000 elements): ", () -> new Object(), 500_000);
        printSize("Object size: ", () -> new Object(), size);

        System.out.println("Arrays (12 bytes): ");
        printSize("1. Array of int (500 000 elements): ", () -> new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 500_000);
        printSize("2. Array of int:", () -> new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, size);
        printSize("3. Array of long: ", () -> new long[]{0L, 1L, 2L, 3L, 4L}, size);
        printSize("4. Array of char[10]: ", () -> new char[10], size);
        printSize("5. Array of char[0]: ", () -> new char[0], size);
    }

    private static <T> void printSize(String text, Supplier<T> supplier, int size) {
        StringBuilder sb = new StringBuilder().append(text).append(MeasuringStand.getSize(supplier, size));
        System.out.println(sb);
    }
}
