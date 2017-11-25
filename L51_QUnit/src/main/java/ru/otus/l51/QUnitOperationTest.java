package ru.otus.l51;

import ru.otus.qunit.annotations.QAfter;
import ru.otus.qunit.annotations.QBefore;
import ru.otus.qunit.annotations.QTest;

import static ru.otus.qunit.QAssert.qassertEquals;

public class QUnitOperationTest {
    @QTest
    public void testAdd() throws Exception {
        qassertEquals(7,QUnitOperation.add(2,5));
    }

    @QTest
    public void testSub() throws Exception {
        qassertEquals(2,QUnitOperation.sub(7,5));
    }

    @QBefore
    public void testBefore() throws Exception {
        System.out.println("Before annotation checked");
    }

    @QBefore
    public void setUp() throws Exception {
        System.out.println("Setup completed");
    }

    @QAfter
    public void testAfter() throws Exception {
        System.out.println("After annotation checked");
    }
}
