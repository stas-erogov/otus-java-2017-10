package ru.otus.l51;

import ru.otus.qunit.QAfter;
import ru.otus.qunit.QBefore;
import ru.otus.qunit.QTest;
import ru.otus.qunit.QUnitOperation;

import static ru.otus.qunit.QAssert.qassertEquals;

public class QUnitOperationTest {
    static int countBefore = 0;
    static int countAfter = 0;
    @QTest
    public void testAdd() throws Exception {
        qassertEquals(7,QUnitOperation.add(2,5));
    }

    @QBefore
    public void testBefore1() throws Exception {
        System.out.println("Before annotation #1 checked times: " + ++countBefore);
    }

    @QBefore
    public void testBefore2() throws Exception {
        System.out.println("Before annotation #2 just sout");
    }

    @QAfter
    public void testAfter() throws Exception {
        System.out.println("After annotation checked times: " + ++countAfter);
    }
}
