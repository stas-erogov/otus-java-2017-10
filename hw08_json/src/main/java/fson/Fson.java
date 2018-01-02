package fson;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Fson {

    public String toJson (Object object) {
        StringWriter writer = new StringWriter();
        TypeAdapter factory = TypeAdapter.getTypeAdapter(object.getClass());
        factory.write(object, writer);

        return writer.toString();
    }

    public <T> T fromJson(String json, Class<?> clazz) {
        Object result;
        JsonValue value = Json.createReader(new StringReader(json)).read();

        result = fromJsonValue(value, clazz);
        return (T) result;
    }

    private static Object fromJsonValue(JsonValue value, Type type) {
        
        if (value.getValueType() == ValueType.NULL) {
            return null;
        }
        else if (value.getValueType() == ValueType.TRUE || value.getValueType() == ValueType.FALSE) {
            return fromBoolean(value, type);
        }
        else if (value instanceof JsonNumber) {
            return fromNumber((JsonNumber) value, type);
        }
        else if (value instanceof JsonString) {
            return fromString((JsonString) value, type);
        }
        else if (value instanceof JsonArray) {
            return fromArray((JsonArray) value, type);
        }
        else if (value instanceof JsonObject) {
            return fromObject((JsonObject) value, type);
        }
        else {
            throw new UnsupportedOperationException("Unsupported json value: " + value);
        }
    }

    private static Object fromObject(JsonObject value, Type type) {
        Class<?> targetClass = (Class<?>) ((type instanceof ParameterizedType) ? ((ParameterizedType) type).getRawType() : type);

        if (Map.class.isAssignableFrom(targetClass)) {
            Class<?> valueClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[1];
            Map<String, Object> map = new LinkedHashMap<>();

            for (Map.Entry<String, JsonValue> entry : value.entrySet()) {
                map.put(entry.getKey(), fromJsonValue(entry.getValue(), valueClass));
            }

            return map;
        }
        else try {
            Object bean = targetClass.newInstance();
            for (PropertyDescriptor property : Introspector.getBeanInfo(targetClass).getPropertyDescriptors()) {
                if (property.getWriteMethod() != null && value.containsKey(property.getName())) {
                    property.getWriteMethod().invoke(bean, fromJsonValue(value.get(property.getName()), property.getWriteMethod().getGenericParameterTypes()[0]));
                }
            }

            return bean;
        }
        catch (Exception e) {
            throw new UnsupportedOperationException("Unsupported object type: " + targetClass, e);
        }
    }

    private static Object fromArray(JsonArray value, Type type) {
        Class<?> targetClass = (Class<?>) ((type instanceof ParameterizedType) ? ((ParameterizedType) type).getRawType() : type);

        if (List.class.isAssignableFrom(targetClass)) {
            Class<?> elementClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
            List<Object> list = new ArrayList<>();

            for (JsonValue item : value) {
                list.add(fromJsonValue(item, elementClass));
            }

            return list;
        }
        else if (targetClass.isArray()) {
            Class<?> elementClass = targetClass.getComponentType();
            Object array = Array.newInstance(elementClass, value.size());

            for (int i = 0; i < value.size(); i++) {
                Array.set(array, i, fromJsonValue(value.get(i), elementClass));
            }

            return array;
        }
        return null;
    }

    private static Object fromString(JsonString value, Type type) {
        if (type == String.class) {
            return value.getString();
        }
        return null;
    }

    private static Object fromNumber(JsonNumber value, Type type) {
        if (type == Integer.class) {
            return value.intValue();
        }
        else if (type == Long.class) {
            return value.longValue();
        }
        else if (type == Double.class) {
            return value.doubleValue();
        }
        else if (type == Float.class) {
            return value.doubleValue();
        }
        return null;
    }

    private static Object fromBoolean(JsonValue value, Type type) {
        if (type == Boolean.class) {
            return Boolean.valueOf(value.toString());
        }
        return null;
    }
}
