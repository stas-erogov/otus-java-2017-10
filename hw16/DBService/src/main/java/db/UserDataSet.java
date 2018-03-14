package db;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "USERS")
public class UserDataSet extends DataSet {

    @Column(name = "NAME")
    private String name;

    @Column(name = "AGE")
    private int age;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS_ID")
    private AddressDataSet address;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, targetEntity = PhoneDataSet.class)
    private Set<PhoneDataSet> phones = new HashSet<>();

    public UserDataSet() {
    }

    public UserDataSet(String name, AddressDataSet address) {
        this.name = name;
        this.address = address;
    }

    public UserDataSet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public UserDataSet(String name, int age, AddressDataSet address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public UserDataSet(String name, AddressDataSet address, PhoneDataSet phoneDataSet) {
        this.name = name;
        this.address = address;
        if (phoneDataSet.getOwner() == null) {
            this.addPhone(phoneDataSet);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public void addPhone(PhoneDataSet phone) {
        this.phones.add(phone);
        if (phone.getOwner() != this) {
            phone.setOwner(this);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserDataSet dataSet = (UserDataSet) object;
        return this.getId() == dataSet.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, age, address);
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
