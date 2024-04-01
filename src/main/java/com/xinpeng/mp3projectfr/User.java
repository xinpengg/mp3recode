package com.xinpeng.mp3projectfr;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "`users`")
public class User {

    @Id
    private String id;
    private double balance;
    private String password;

    @ElementCollection
    @CollectionTable(name = "user_shares", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "outcome")
    @Column(name = "quantity")
    private Map<String, Integer> shares;

    public User(String id, double balance) {
        this.id = id;
        this.balance = balance;
        this.shares = new HashMap<>();
    }

    public User() {
    }

    public double getBalance() {
        return balance;
    }
    public void setPassword(String password) {
        this.password = password;
    }

        public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getTotalShares() {
        return shares.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getShares(String outcome) {
        return shares.getOrDefault(outcome, 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", balance=" + balance +
                ", shares=" + shares +
                '}';
    }

    public void addShares(String outcome, int quantity) {
        shares.put(outcome, getShares(outcome) + quantity);
    }

    public void removeShares(String outcome, int quantity) {
        int currentShares = getShares(outcome);
        if (currentShares < quantity) {
            throw new IllegalArgumentException("Not enough shares to sell");
        }
        shares.put(outcome, currentShares - quantity);
    }

    public String getPassword() {
        return password;

    }
}
