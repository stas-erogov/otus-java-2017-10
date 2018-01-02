package fson.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReflectionHelper {
    public static List<Field> getAllFields (Object obj) {
        Class<?> clazz = obj.getClass();
        List<Field> fields = new ArrayList<>();

        do {
            Collections.addAll(fields, clazz.getDeclaredFields());
        } while ((clazz = clazz.getSuperclass()) != null);

        return fields;
    }

    public static Object getFieldValue(Object obj, Field field) {
        boolean isAccessible = true;
        try {
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
        return null;
    }

    public static boolean isTransient(Field f) {
        return Modifier.isTransient(f.getModifiers());
    }

    static private Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args)
                .map(Object::getClass).toArray(Class<?>[]::new);
    }
}
