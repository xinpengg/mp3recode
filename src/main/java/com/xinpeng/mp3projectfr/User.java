package com.xinpeng.mp3projectfr;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private double balance;
    private Map<String, Integer> shares;


    public User(String id, double balance) {
        this.id = id;
        this.balance = balance;
        this.shares = new HashMap<>();
    }

    public double getBalance() {
        return balance;
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
}
