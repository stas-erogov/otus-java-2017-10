import data.DataObject;
import fson.Fson;
import fson.FsonFromJson;

public class Main {
    public static void main(String...arg) {
        Fson fson = new Fson();

        DataObject dataObjectBefore = new DataObject();
        dataObjectBefore.setSourceData("The Ultimate Question of Life, the Universe, and Everything");
        System.out.println(fson.toJson(dataObjectBefore));

        String jsonn= "{\"id\":42,\"text\":\"The answer\"," +
                "\"things\":[\"showel\",\"guide\",\"Babel fish\"]," +
                "\"doList\":[\"showel\",\"guide\",\"Babel fish\",\"Marvin\"]," +
                "\"mapTest\":{\"one\":1,\"two\":2,\"three\":3}," +
                "\"co\":{\"id\":13,\"b\":true}," +
                "\"field\":\"This is public!\",\"isDataObject\":true,\"falseTest\":false," +
                "\"sourceData\":\"The Ultimate Question of Life, the Universe, and Everything\"," +
                "\"hiddenId\":1138,\"thatsDouble\":3.3333332538604736}\n";

        DataObject dataObjectAfter = FsonFromJson.fromJson(jsonn, DataObject.class);
        System.out.println(fson.toJson(dataObjectAfter));
    }
}
