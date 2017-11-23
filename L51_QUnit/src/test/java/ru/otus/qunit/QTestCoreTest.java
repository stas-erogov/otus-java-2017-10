package ru.otus.qunit;

import org.junit.Test;

import static org.junit.Assert.*;

public class QTestCoreTest {
    @Test(expected = IllegalArgumentException.class)
    public void executeQTest1() throws Exception {
        QTestCore testCore = new QTestCore();
        QTestCore.executeQTest((String)null);
    }

    @Test(expected = ClassNotFoundException.class)
    public void executeQTest2() throws Exception {
        QTestCore testCore = new QTestCore();
        QTestCore.executeQTest("");
    }

    @Test(expected = ClassNotFoundException.class)
    public void executeQTest3() throws Exception {
        QTestCore testCore = new QTestCore();
        QTestCore.executeQTest("QString");
    }

    @Test
    public void executeQTest4() throws Exception {
    }

}