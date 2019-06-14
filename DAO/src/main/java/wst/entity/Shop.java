package wst.entity;

public class Shop {
    private long id;
    private String name;
    private Boolean isActive;
    private String city;
    private String address;
    private String type;

    public Shop() { }

    public Shop(String name, String address, Boolean isActive, String type, String city) {
        this.name = name;
        this.isActive = isActive;
        this.city = city;
        this.address = address;
        this.type = type;
    }

    public Shop(long id, String name, String address, Boolean isActive, String type, String city) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.city = city;
        this.address = address;
        this.type = type;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", isActive=" + isActive +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
