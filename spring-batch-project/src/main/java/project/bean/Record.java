package project.bean;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "record")
public class Record {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "username")
    private String username;

    @XmlElement(name = "date")
    private String date;

    @XmlElement(name = "amount")
    private double amount;

    public Record() {
    }

    public Record(int id, String username,  String date, double amount) {
        this.id = id;
        this.username = username;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction [username=" + username + ", id=" + id
                + ", date=" + date + ", amount=" + amount + "]";
    }
}
