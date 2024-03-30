package com.xinpeng.mp3projectfr;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class BettingService extends Market {
    private Market market;
    private Map<String, User> users;
    public BettingService() {
        super(10000,10000);
        this.users = new HashMap<>();
    }
    public BettingService(int someValue) {
        super(someValue);
        this.users = new HashMap<>();
    }
    public BettingService(Market market) {
        this.market = market;
        this.users = new HashMap<>();
        // Initialize the state as needed
    }


    public BettingService(int yesLiquidity, int noLiquidity) {
        super(yesLiquidity, noLiquidity);
        this.users = new HashMap<>();
    }

    public void registerUser(String userId, double initialBalance) {
        users.put(userId, new User(userId, initialBalance));
        System.out.println("User " + userId + " registered with balance: " + initialBalance);
    }
    public void sellShares(String userId, String outcome, int shares) {
        outcome = outcome.toUpperCase();
        User user = users.get(userId);
        System.out.println("User " + userId + " is buying " + shares + " " + outcome + " shares.");

//        if (user == null) {
//            System.out.println("Transaction failed. User not found.");
//            return;
//        }

        double cost = super.sellShares(outcome, shares);
        if (cost <= 0 || user.getBalance() < cost || user.getTotalShares() < shares) {
            System.out.println("Transaction failed. Invalid sale or insufficient balance.");
            return;
        }

        user.setBalance(user.getBalance() + cost);
        user.removeShares(outcome, shares);
        System.out.println("User " + userId + " sold " + shares + " " + outcome + " shares for " + cost);
    }


    public void buyShares(String userId, String outcome, int shares) {
        outcome = outcome.toUpperCase();

        User user = users.get(userId);
        System.out.println("User " + userId + " is buying " + shares + " " + outcome + " shares.");
//        if (user == null) {
//            System.out.println("Transaction failed. User not found.");
//            return;
//        }

        double cost = super.buyShares(outcome, shares);

        if (cost <= 0 || user.getBalance() < cost) {
            System.out.println("Transaction failed. Invalid purchase or insufficient balance.");
            return;
        }

        user.setBalance(user.getBalance() - cost);
        user.addShares(outcome, shares);
        System.out.println("User " + userId + " bought " + shares + " " + outcome + " shares for " + cost);
    }
    public void resolveBet(String winningOutcome) {
        winningOutcome = winningOutcome.toUpperCase();
        String losingOutcome = winningOutcome.equals("YES") ? "NO" : "YES";

        for (User user : users.values()) {
            int winningShares = user.getShares(winningOutcome);
            int losingShares = user.getShares(winningOutcome.equals("YES") ? "NO" : "YES");

            double profit = winningShares * 1.0;
            double loss = losingShares * 1.0;
            user.setBalance(user.getBalance() + profit);

            if (winningShares > 0) {
                System.out.println("User " + user.getId() + " won " + profit + " from " + winningShares + " " + winningOutcome + " shares.");
            }
            if (losingShares > 0) {
                user.setBalance(user.getBalance() - loss);
                System.out.println("User " + user.getId() + " lost " + loss + " from " + losingOutcome + " shares.");
            }

            user.removeShares("YES", user.getShares("YES"));
            user.removeShares("NO", user.getShares("NO"));
        }
    }


    public void printTotalShares(String userId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("User " + userId + " has " + user.getTotalShares() + " total shares.");
    }

}