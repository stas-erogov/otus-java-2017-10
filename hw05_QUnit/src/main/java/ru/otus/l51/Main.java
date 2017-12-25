package ru.otus.l51;

import java.io.IOException;

import static ru.otus.qunit.QTestCore.executeQTest;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Execute test for class:");
            executeQTest("ru.otus.l51.QUnitOperationTest");
            System.out.println("");

            System.out.println("Execute test for package:");
            executeQTest("ru.otus.l51");
        } catch (ClassNotFoundException e) {
            System.out.println("Incorrect class name");
            e.printStackTrace();
        } catch ( IOException e ) {
            System.out.println("Incorrect package name");
            e.printStackTrace();
        }
    }
}
