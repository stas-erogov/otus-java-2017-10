package ru.otus.qunit;

import org.junit.Test;

import static ru.otus.qunit.QAssert.qassertEquals;

public class QAssertTest {
    @Test(expected = Error.class)
    public void qassertEquals1() throws Exception {
        qassertEquals("Error", "qwe", "rty");
    }

    @Test(expected = Error.class)
    public void qassertEquals2() throws Exception {
        qassertEquals("qwe", "rty");
    }

    @Test
    public void qassertEquals3() throws Exception {
        qassertEquals(10, 10);
    }
}