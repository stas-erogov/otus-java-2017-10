package fson;

import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class ArrayType extends TypeAdapter {
    @Override
    public void write(Object object, StringWriter writer) {
        writer.write("[");

        Collection collection;
        if (object.getClass().isArray()) {
            collection = Arrays.asList((Object[]) object);
        } else {
            collection = (Collection) object;
        }
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Object e = it.next();
            TypeAdapter adapter = TypeAdapter.getTypeAdapter(e.getClass());
            adapter.write(e, writer);
            if (it.hasNext()) {
                writer.write(",");
            }
        }
        writer.write("]");
    }

    @Override
    public void write(Object object, JsonGenerator generator) {
        generator.writeStartArray();
        JsonGeneratorAdapter adapter = JsonGeneratorAdapter.getGenerator(object.getClass());
        adapter.write(object, generator);
        generator.writeEnd();
    }

    @Override
    public void write(String name, Object object, JsonGenerator generator) {
        generator.writeStartArray(name);
        JsonGeneratorAdapter adapter = JsonGeneratorAdapter.getGenerator(object.getClass());
        adapter.write(object, generator);
        generator.writeEnd();
    }
}
