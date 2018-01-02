package data;

import java.util.Objects;

public class SourceObject {
    private String sourceData = "";
    private int hiddenId = 1138;
    private double thatsDouble = 10f/3;

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SourceObject that = (SourceObject) object;
        return hiddenId == that.hiddenId &&
                Double.compare(that.thatsDouble, thatsDouble) == 0 &&
                Objects.equals(sourceData, that.sourceData);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sourceData, hiddenId, thatsDouble);
    }
}
