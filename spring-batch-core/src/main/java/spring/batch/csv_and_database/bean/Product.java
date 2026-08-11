package spring.batch.csv_and_database.bean;

public class Product {

    private String name;
    private String value;

    public Product() {
    }

    public Product(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
