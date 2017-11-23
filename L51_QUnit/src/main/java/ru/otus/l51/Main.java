package ru.otus.l51;

import static ru.otus.qunit.QTestCore.executeQTest;

public class Main {
    public static void main(String[] args) {
        try {
            executeQTest("ru.otus.l51.QUnitOperationTest");
        } catch (ClassNotFoundException e) {
            System.out.println("Incorrect class name");
            e.printStackTrace();
        }
    }
}
