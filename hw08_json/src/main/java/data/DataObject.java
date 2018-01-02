package data;

import java.util.*;

public class DataObject extends SourceObject {
    private int id = 42;
    public String nullCheck = null;
    private String text = "The answer"; //STRING

    private String[] things = {"showel", "guide", "Babel fish"}; //ARRAY
    List<String> doList = new ArrayList<>(Arrays.asList(things)); //ARRAY
    Map<String, Integer> mapTest = new HashMap<>();

    public DataObject () {
        doList.add("Marvin");
        mapTest.put("one", 1);
        mapTest.put("two", 2);
        mapTest.put("three", 3);
    } //ARRAY

    CustomObject co = new CustomObject(); //OBJECT

    public String field = "This is public!"; //STRING
    public boolean isDataObject = true; //true
    public boolean falseTest = false; //false

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DataObject that = (DataObject) object;
        return id == that.id &&
                isDataObject == that.isDataObject &&
                falseTest == that.falseTest &&
                Objects.equals(nullCheck, that.nullCheck) &&
                Objects.equals(text, that.text) &&
                Arrays.equals(things, that.things) &&
                Objects.equals(doList, that.doList) &&
                Objects.equals(mapTest, that.mapTest) &&
                Objects.equals(co, that.co) &&
                Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, nullCheck, text, doList, mapTest, co, field, isDataObject, falseTest);
        result = 31 * result + Arrays.hashCode(things);
        return result;
    }
}
