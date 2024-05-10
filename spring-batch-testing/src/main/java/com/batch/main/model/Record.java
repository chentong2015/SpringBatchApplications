package com.batch.main.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;

@XmlRootElement(name = "record")
public class Record {

    @XmlElement(name = "username")
    private String username;

    @XmlElement(name = "userId")
    private int userId;

    @XmlElement(name = "date")
    private LocalDate date;

    @XmlElement(name = "amount")
    private double amount;

    public Record() {
    }

    public Record(String username, int userId, LocalDate date, double amount) {
        this.username = username;
        this.userId = userId;
        this.date = date;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        return "Transaction [username=" + username + ", userId=" + userId
                + ", date=" + date + ", amount=" + amount + "]";
    }
}
