package fson;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.util.Map;

public class MapType extends TypeAdapter {
    @Override
    public void write(Object object, StringWriter writer) {
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartObject();
        for (Map.Entry<String, Object> entry : ((Map<String, Object>)object).entrySet() ) {
            writeMap(entry.getKey(), entry.getValue(), generator);
        }
        generator.writeEnd();
        generator.close();
    }

    @Override
    public void write(Object object, JsonGenerator generator) {
        generator.writeStartObject();
        for (Map.Entry<String, Object> entry : ((Map<String, Object>)object).entrySet() ) {
            write(entry.getKey(), entry.getValue(), generator);
        }
        generator.writeEnd();
        JsonGeneratorAdapter adapter = JsonGeneratorAdapter.getGenerator(object.getClass());
        adapter.write(object, generator);
    }

    @Override
    public void write(String name, Object object, JsonGenerator generator) {
        generator.writeStartObject(name);
        JsonGeneratorAdapter adapter = JsonGeneratorAdapter.getGenerator(Map.class);
        adapter.write(object, generator);
        generator.writeEnd();
    }


    public void writeMap(String name, Object object, JsonGenerator generator) {
        TypeAdapter adapter = TypeAdapter.getTypeAdapter(object.getClass());
        adapter.write(name, object, generator);
    }
}
