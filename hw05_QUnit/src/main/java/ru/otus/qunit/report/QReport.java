package ru.otus.qunit.report;

import ru.otus.qunit.TestStatus;

public interface QReport {
    void writeTestResult(String methodName, TestStatus status);

    void printResults();
}
