package project.base_bean;

public class DbRecord {

    private int id;
    private String username;
    private double amount;

    public DbRecord() {
    }

    public DbRecord(int id, String username, double amount) {
        this.id = id;
        this.username = username;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RecordDB{" +
                "username='" + username + '\'' +
                ", Id=" + id +
                ", amount=" + amount +
                '}';
    }
}
