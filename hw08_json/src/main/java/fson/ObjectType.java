package fson;

import fson.reflection.ReflectionHelper;

import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObjectType extends TypeAdapter {
    private final Map<String, Object> map = new LinkedHashMap<>();

    ObjectType() {
        super();
    }

    @Override
    public void write(Object object, StringWriter writer) {
        this.fillMap(object);
        TypeAdapter adapter = TypeAdapter.getTypeAdapter(Map.class);
        adapter.write(map, writer);

    }

    @Override
    public void write(Object object, JsonGenerator generator) {
        this.fillMap(object);
        generator.writeStartArray();
        JsonGeneratorAdapter adapter = JsonGeneratorAdapter.getGenerator(Map.class);
        adapter.write(map, generator);
        generator.writeEnd();
    }

    @Override
    public void write(String name, Object object, JsonGenerator generator) {
        this.fillMap(object);
        generator.writeStartObject(name);
        JsonGeneratorAdapter adapter = JsonGeneratorAdapter.getGenerator(Map.class);
        adapter.write(map, generator);
        generator.writeEnd();
    }

    private void fillMap(Object object) {
        List<Field> fields = ReflectionHelper.getAllFields(object);
        for (Field f : fields) {
            if (!ReflectionHelper.isTransient(f) && ReflectionHelper.getFieldValue(object, f)!=null) {
                map.put(f.getName(), ReflectionHelper.getFieldValue(object, f));
            }
        }
    }
}
