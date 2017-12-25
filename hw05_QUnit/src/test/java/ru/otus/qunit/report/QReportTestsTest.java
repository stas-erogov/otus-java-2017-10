package ru.otus.qunit.report;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.otus.qunit.TestStatus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class QReportTestsTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void setUp () {
        System.setOut(new PrintStream(out));
    }

    @Test
    public void writeTestResult() throws Exception {
        QReportTests rep = new QReportTests();
        rep.writeTestResult("Test#1", TestStatus.FAILED);
        rep.writeTestResult("Test#2", TestStatus.SUCCESS);
        assertEquals(2, rep.getTotalTests());
        assertEquals(1, rep.getCountSuccessTests());
        assertEquals(1, rep.getCountFailTests());
    }

    @Test
    public void printResults() throws Exception {
        QReportTests rep = new QReportTests();
        rep.writeTestResult("Test", TestStatus.SUCCESS);
        rep.printResults();

        String output = new String(out.toByteArray());
        assertTrue(output.contains("Results:"));
        assertTrue(output.contains("Total tests: 1, Success tests: 1, Failed tests: 0"));
        assertTrue(output.contains("Test - SUCCESS"));
    }

    @After
    public void cleanUp() {
        System.setOut(null);
    }
}