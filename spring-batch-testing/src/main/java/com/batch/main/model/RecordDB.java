package com.batch.main.model;

public class RecordDB {

    private String username;
    private int id;
    private double amount;

    public RecordDB() {
    }

    public RecordDB(String username, int id, double amount) {
        this.username = username;
        this.id = id;
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

    public void setId(int userId) {
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
