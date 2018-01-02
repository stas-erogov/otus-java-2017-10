package data;

import java.util.Objects;

public class CustomObject {
    private long id = 13;
    private boolean b = true;
    private transient String name = "Wild";

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CustomObject that = (CustomObject) object;
        return id == that.id &&
                b == that.b &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, b, name);
    }
}
