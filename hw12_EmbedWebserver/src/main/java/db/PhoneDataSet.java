package db;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "PHONES")
public class PhoneDataSet extends DataSet {
    @Column(name = "PHONE_NUM")
    private String phoneNum;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = UserDataSet.class)
    @JoinColumn(name = "OWNER_ID")
    private UserDataSet owner;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public PhoneDataSet(String phoneNum, UserDataSet owner) {
        this.phoneNum = phoneNum;
        this.owner = owner;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public UserDataSet getOwner() {
        return owner;
    }

    public void setOwner(UserDataSet owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PhoneDataSet phone = (PhoneDataSet) object;
        return Objects.equals(this.getId(), phone.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(phoneNum);
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "phoneNum='" + phoneNum +
                '}';
    }
}
