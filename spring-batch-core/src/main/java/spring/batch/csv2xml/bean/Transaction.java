package spring.batch.csv2xml.bean;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;

// 定义XML Root头标签的名称
@XmlRootElement(name = "transaction")
@XmlAccessorType(XmlAccessType.FIELD)
public class Transaction {

    private String username;
    private int id;

    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDate date;

    private double amount;

    public Transaction() {
    }

    public Transaction(String username, int id, LocalDate date, double amount) {
        this.username = username;
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
