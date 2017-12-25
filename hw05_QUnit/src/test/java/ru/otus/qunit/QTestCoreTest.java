package ru.otus.qunit;

import org.junit.Test;

import static ru.otus.qunit.QTestCore.executeQTest;

public class QTestCoreTest {
    @Test(expected = IllegalArgumentException.class)
    public void executeQTest1() throws Exception {
        executeQTest(null);
    }

    @Test(expected = ClassNotFoundException.class)
    public void executeQTest2() throws Exception {
        executeQTest("ru.otus.l51.QUnitOperationTested");
    }

    @Test(expected = ClassNotFoundException.class)
    public void executeQTest3() throws Exception {
        executeQTest("ru.otus.l51");
    }
}