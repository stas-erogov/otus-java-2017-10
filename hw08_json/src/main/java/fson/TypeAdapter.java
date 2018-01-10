package fson;

import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public abstract class TypeAdapter<T> {

    TypeAdapter() {
    }

    public abstract void write(Object object, StringWriter writer);
    public abstract void write(Object object, JsonGenerator generator);
    public abstract void write(String name, Object object, JsonGenerator generator);

    public static TypeAdapter getTypeAdapter(Class<?> clazz) {

        Types types = Types.getType(clazz);
        switch (types) {
            case BOOLEAN:
                return new TypeAdapter<Boolean>() {
                    @Override
                    public void write(Object object, StringWriter writer) {
                        writer.write(String.valueOf(object));
                    }

                    @Override
                    public void write(Object object, JsonGenerator generator) {
                        generator.write((Boolean) object);
                    }

                    @Override
                    public void write(String name, Object object, JsonGenerator generator) {
                        generator.write(name, (Boolean) object);
                    }
                };
            case STRING:
                return new StringType();
            case ARRAY:
                return new ArrayType();
            case OBJECT:
                if (Map.class.isAssignableFrom(clazz)) {
                    return new MapType();
                }
                return new ObjectType();
            case NUMBER:
                if (clazz == Byte.class || clazz == Short.class || clazz == Integer.class) {
                    return new IntType();
                } else if (clazz == Character.class) {
                    return new TypeAdapter<Character>() {
                        @Override
                        public void write(Object object, StringWriter writer) {
                            writer.write("\"" + Character.toString((Character) object) + "\"");
                        }

                        @Override
                        public void write(Object object, JsonGenerator generator) {
                            generator.write(Character.toString((Character) object));
                        }

                        @Override
                        public void write(String name, Object object, JsonGenerator generator) {
                            generator.write(name, Character.toString((Character) object));
                        }
                    };
                } else if (clazz == Long.class) {
                    return new LongType();
                } else {
                    return new DoubleType();
                }

        }
        throw new RuntimeException();
    }

    static class StringType extends TypeAdapter {
        @Override
        public void write(Object object, StringWriter writer) {
            writer.write("\""+object + "\"");
        }

        @Override
        public void write(Object object, JsonGenerator generator) {
            generator.write((String)object);
        }

        @Override
        public void write(String name, Object object, JsonGenerator generator) {
            generator.write(name, (String)object);
        }
    }

    static class DoubleType extends TypeAdapter {
        @Override
        public void write(Object object, StringWriter writer) {
            writer.write(object.toString());
        }

        @Override
        public void write(Object object, JsonGenerator generator) {
            generator.write((Double) object);
        }

        @Override
        public void write(String name, Object object, JsonGenerator generator) {
            generator.write(name, (Double) object);
        }
    }

    static class IntType extends TypeAdapter {
        @Override
        public void write(Object object, StringWriter writer) {
            writer.write(((Integer) object).toString());
        }

        @Override
        public void write(Object object, JsonGenerator generator) {
            generator.write((Integer) object);
        }

        @Override
        public void write(String name, Object object, JsonGenerator generator) {
            generator.write(name, (Integer) object);
        }
    }
    static class LongType extends TypeAdapter {
        @Override
        public void write(Object object, StringWriter writer) {
            writer.write(((Long) object).toString());
        }

        @Override
        public void write(Object object, JsonGenerator generator) {
            generator.write((Long) object);
        }

        @Override
        public void write(String name, Object object, JsonGenerator generator) {
            generator.write(name, (Long) object);
        }
    }

    enum Types {
        BOOLEAN,
        NUMBER,
        STRING,
        ARRAY,
        OBJECT;

        private static final String ALL_TYPES_STRING = Arrays.toString(Types.values());

        public static Types getType(Class<?> clazz) {
            String className = clazz.getSimpleName().toUpperCase();
            if (ALL_TYPES_STRING.contains(className)) {
                return Types.valueOf(className);
            } else if (Number.class.isAssignableFrom(clazz)) {
                return Types.NUMBER;
            } else if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
                return Types.ARRAY;
            } else {
                return Types.OBJECT;
            }
        }
    }
}
