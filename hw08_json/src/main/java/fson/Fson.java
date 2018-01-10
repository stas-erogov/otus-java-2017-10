package fson;

import java.io.StringWriter;

public class Fson {

    public String toJson (Object object) {
        StringWriter writer = new StringWriter();
        TypeAdapter adapter = TypeAdapter.getTypeAdapter(object.getClass());
        adapter.write(object, writer);

        return writer.toString();
    }
}
