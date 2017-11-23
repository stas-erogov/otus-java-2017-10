package ru.otus.qunit;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class QTestCore {

    private static Map<String, TestStatus> tests = new HashMap<>();
    private static int countSuccessTests = 0;

    public QTestCore() {
    }

    public static void executeQTest(String className) throws ClassNotFoundException {
        if (className == null)
            throw new IllegalArgumentException("Argument is null");

        Class<?> clazz = null;
        clazz = Class.forName(className);
        executeQTest(clazz);
    }

    public static void executeQTest(Class<?> clazz) {
        Object testObject = ReflectionHelper.instantiate(clazz);
        runTests(clazz, testObject);
        printResults();
    }

    private static void runTests(Class<?> clazz, Object testObject) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> beforeTestMethods = collectAnnotatedMethods(methods, QBefore.class);;
        List<Method> afterTestMethods = collectAnnotatedMethods(methods, QAfter.class);

        for (Method method : methods) {
            if (method.isAnnotationPresent(QTest.class)) {
                beforeTestMethods.forEach(m -> {
                    try {
                        ReflectionHelper.callMethod(testObject, m.getName(), m.getParameters());
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
                try {
                    ReflectionHelper.callMethod(testObject, method.getName(), method.getParameters());
                    tests.put(clazz.getSimpleName(), TestStatus.SUCCESS);
                    countSuccessTests++;
                } catch (Exception e) {
                    tests.put(clazz.getSimpleName(), TestStatus.FAILED);
                }
                afterTestMethods.forEach(m -> {
                    try {
                        ReflectionHelper.callMethod(testObject, m.getName(), m.getParameters());
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private static List<Method> collectAnnotatedMethods (Method[] methods, Class<? extends Annotation> clazz) {
        List<Method> list = Arrays.asList(methods)
                .stream()
                .filter(l->l.isAnnotationPresent(clazz))
                .collect(Collectors.toList());
        return list;
    }

    private static void printResults() {
        System.out.println("Test completed. ");
        System.out.println(" Total tests: " + tests.size());
        System.out.println(" Success tests: " + countSuccessTests);
        System.out.println(" Failed tests: " + (tests.size() - countSuccessTests));

        for (Map.Entry<String, TestStatus> entry : tests.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}
