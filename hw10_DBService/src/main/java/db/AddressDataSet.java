package db;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity(name = "ADDRESSES")
public class AddressDataSet extends DataSet{

    @Column(name = "POSTCODE")
    private String postcode;

    @Column(name = "ADDRESS")
    private String address;

    public AddressDataSet() {
    }

    public AddressDataSet(String postcode, String address) {
        this.postcode = postcode;
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AddressDataSet that = (AddressDataSet) object;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(postcode, address);
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "postcode='" + postcode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
