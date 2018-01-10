package fson;

import com.google.gson.Gson;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class FsonTest {

    final String jsonn= "{\"id\":42,\"text\":\"The answer\"," +
            "\"things\":[\"showel\",\"guide\",\"Babel fish\"]," +
            "\"doList\":[\"showel\",\"guide\",\"Babel fish\",\"Marvin\"]," +
            "\"mapTest\":{\"one\":1,\"two\":2,\"three\":3}," +
            "\"co\":{\"id\":13,\"b\":true}," +
            "\"field\":\"This is public!\",\"isDataObject\":true,\"falseTest\":false," +
            "\"sourceData\":\"The Ultimate Question of Life, the Universe, and Everything\"," +
            "\"hiddenId\":1138,\"thatsDouble\":3.3333332538604736}\n";

    @Test
    public void testObjToJson() throws JSONException {
        data.DataObject dataObject = new data.DataObject();

        String gsonJson = new Gson().toJson(dataObject);
        String fsonJson = new Fson().toJson(dataObject);

        JSONAssert.assertEquals(gsonJson, fsonJson, true);
    }

    @Test
    public void testListToJson() throws JSONException {
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add("one");
        list.add(new data.DataObject());

        String gsonJson = new Gson().toJson(list);
        String fsonJson = new Fson().toJson(list);

        JSONAssert.assertEquals(gsonJson, fsonJson, true);
    }

    @Test
    public void fromJson() {
        data.DataObject dataObject1 = FsonFromJson.fromJson(jsonn, data.DataObject.class);
        data.DataObject dataObject2 = new Gson().fromJson(jsonn, data.DataObject.class);

        assertTrue(dataObject1.equals(dataObject2));
    }
}