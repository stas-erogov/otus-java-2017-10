package ru.otus.qunit.report;

import ru.otus.qunit.TestStatus;

import java.util.HashMap;
import java.util.Map;

public class QReportTests implements QReport {
    private int countSuccessTests = 0;
    private Map<String, TestStatus> tests = new HashMap<>();

    public void printResults() {
        System.out.println("Results:");
        System.out.print(" Total tests: " + getTotalTests() + ",");
        System.out.print(" Success tests: " + getCountSuccessTests() + ",");
        System.out.print(" Failed tests: " + getCountFailTests());
        System.out.println("");

        tests.forEach((k, v) -> System.out.println(k + " - " + v));
    }

    public int getCountSuccessTests() {
        return countSuccessTests;
    }

    public int getCountFailTests() {
        return tests.size() - countSuccessTests;
    }

    public int getTotalTests() {
        return tests.size();
    }

    public void writeTestResult (String methodName, TestStatus status) {
        tests.put(methodName, status);
        if (status.equals(TestStatus.SUCCESS)) {
            countSuccessTests++;
        }
    }
}
