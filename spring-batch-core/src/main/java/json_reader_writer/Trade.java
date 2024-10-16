package json_reader_writer;

public class Trade {

    private int isin;
    private int quantity;
    private float price;
    private String customer;

    public Trade() {
    }

    public Trade(int isin, int quantity, float price, String customer) {
        this.isin = isin;
        this.quantity = quantity;
        this.price = price;
        this.customer = customer;
    }

    public int getIsin() {
        return isin;
    }

    public void setIsin(int isin) {
        this.isin = isin;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
