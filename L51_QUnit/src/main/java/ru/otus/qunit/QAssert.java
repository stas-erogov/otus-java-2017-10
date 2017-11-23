package ru.otus.qunit;

public class QAssert {
    public static void qassertEquals(String s, Object expected, Object actual) {
        if (expected != null && actual != null) {
            if (!expected.equals(actual)) {
                s = s.equals(null) ? "Assertion Error" : s;
                throw new Error(s);
            }
        }
    }

    public static <T> void qassertEquals(T a, T b) {
        qassertEquals(null, a, b);
    }
}
