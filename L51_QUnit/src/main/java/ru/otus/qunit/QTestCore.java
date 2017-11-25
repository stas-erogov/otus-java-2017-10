package ru.otus.qunit;

import com.google.common.reflect.ClassPath;
import ru.otus.qunit.annotations.QAfter;
import ru.otus.qunit.annotations.QBefore;
import ru.otus.qunit.annotations.QTest;
import ru.otus.qunit.report.QReport;
import ru.otus.qunit.report.QReportTests;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

public class QTestCore {

    public QTestCore() {
    }

    public static void executeQTest(String name) throws ClassNotFoundException, IOException {
        if (name == null)
            throw new IllegalArgumentException("Argument is null");

        QReport report = new QReportTests();

        if (Package.getPackage(name) != null) {
            executeQTestByPackage(name);
        } else {
            Class<?> clazz = Class.forName(name);
            executeQTest(clazz, report);
        }
    }

    private static void executeQTestByPackage(String packageName) throws IOException {//ClassLoader.getSystemClassLoader()
        Set<ClassPath.ClassInfo> classes = ClassPath
                .from(Thread.currentThread().getContextClassLoader())
                .getTopLevelClasses(packageName);

        Set<Class<?>> testClasses = classes.stream()
                .map(ci -> {
                    try {
                        return Class.forName(ci.getName());
                    } catch ( ClassNotFoundException e ) {
                        return null;
                    }
                })
                .filter(c -> Arrays.stream(c.getDeclaredMethods())
                        .anyMatch(m -> m.isAnnotationPresent(QTest.class)))
                .collect(Collectors.toSet());

        testClasses.forEach(c -> {
            try {
                executeQTest(c.getName());
            } catch ( ClassNotFoundException | IOException e ) {
                e.printStackTrace();
            }
        });
    }

    private static void executeQTest(Class<?> clazz, QReport report) {
        Object testObject = ReflectionHelper.instantiate(clazz);
        runTests(clazz, testObject, report);
        report.printResults();
    }

    private static void runTests(Class<?> clazz, Object testObject, QReport report) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> beforeTestMethods = collectAnnotatedMethods(methods, QBefore.class);
        List<Method> afterTestMethods = collectAnnotatedMethods(methods, QAfter.class);

        for (Method method : methods) {
            if (method.isAnnotationPresent(QTest.class)) {
                beforeTestMethods.forEach(m -> {
                    try {
                        ReflectionHelper.callMethod(testObject, m.getName());
                    } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException e ) {
                        e.printStackTrace();
                    }
                });
                try {
                    ReflectionHelper.callMethod(testObject, method.getName());
                    report.writeTestResult(method.toString(), TestStatus.SUCCESS);
                } catch ( Exception e ) {
                    report.writeTestResult(method.toString(), TestStatus.FAILED);
                }
                afterTestMethods.forEach(m -> {
                    try {
                        ReflectionHelper.callMethod(testObject, m.getName());
                    } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException e ) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private static List<Method> collectAnnotatedMethods(Method[] methods, Class<? extends Annotation> clazz) {
        return Arrays.stream(methods)
                .filter(l -> l.isAnnotationPresent(clazz))
                .collect(Collectors.toList());
    }
}
