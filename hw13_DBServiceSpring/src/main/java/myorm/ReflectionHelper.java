package myorm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReflectionHelper {

    static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.newInstance();
            } else {
                return type.getConstructor(toClasses(args)).newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Field[] getAnnotatedFields (Class<?> clazz, Class<? extends Annotation> type) {
        return ReflectionHelper.getAllFields(clazz).stream()
                .filter(f->f.isAnnotationPresent(type))
                .toArray(Field[]::new);
    }

    static Object getFieldValue(Object object, Field field) {
        boolean isAccessible = true;
        try {
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
        return null;
    }

    static void setFieldValue(Object object, Field field, Object value) {
        boolean isAccessible = true;
        try {
            isAccessible = field.isAccessible();
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    public static List<Field> getAllFields (Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        do {
            List<Field> buf = new ArrayList<>();

            Collections.addAll(buf, clazz.getDeclaredFields());
            fields.addAll(0, buf);
        } while ((clazz = clazz.getSuperclass()) != null);

        return fields;
    }

    static private Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args)
                .map(Object::getClass).toArray(Class<?>[]::new);
    }
}
