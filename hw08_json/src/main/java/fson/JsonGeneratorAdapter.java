package fson;

import javax.json.stream.JsonGenerator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public abstract class JsonGeneratorAdapter<T> {
    public JsonGeneratorAdapter(){
    }

    public abstract void write(Object object, JsonGenerator generator);

    public static JsonGeneratorAdapter getGenerator(Class<?> clazz) {
        TypeAdapter.Types types = TypeAdapter.Types.getType(clazz);
        switch (types) {
            case ARRAY:
                return new ArrayGeneratorAdapter();
            case OBJECT:
                if (Map.class.isAssignableFrom(clazz)) {
                    return new MapGeneratorAdapter();
                }
                return null;
            default:
                return null;
        }
    }

    private static class ArrayGeneratorAdapter extends JsonGeneratorAdapter {
        @Override
        public void write(Object object, JsonGenerator generator) {
            Collection collection;
            if (object.getClass().isArray()) {
                collection = Arrays.asList((Object[]) object);
            } else {
                collection = (Collection) object;
            }
            for (Object e : collection) {
                TypeAdapter adapter = TypeAdapter.getTypeAdapter(e.getClass());
                adapter.write(e, generator);
            }
        }
    }

    private static class MapGeneratorAdapter extends JsonGeneratorAdapter {
        @Override
        public void write(Object object, JsonGenerator generator) {

            for (Map.Entry<String, Object> entry : ((Map<String, Object>)object).entrySet() ) {
                TypeAdapter adapter = TypeAdapter.getTypeAdapter(entry.getValue().getClass());
                adapter.write(entry.getKey(),entry.getValue(),generator);
            }
        }
    }
}
