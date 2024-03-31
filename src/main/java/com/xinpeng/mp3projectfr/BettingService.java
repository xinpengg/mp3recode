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
    public BettingService(UserService userService) {
        super(10000,10000);
        this.users = new HashMap<>();
    }

    // Constructor that doesn't directly initialize userService, calls another constructor that does
    public BettingService(int someValue) {
super (someValue, someValue);
        this.users = new HashMap<>();
    }

    // Another constructor that doesn't directly initialize userService, calls another constructor that does
    public BettingService(Market market, UserService userService) {
        this(userService);  // Call the constructor that initializes userService
        this.market = market;
        // additional initialization
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
    public User findUserById(String userId) {
        System.out.println("Trying to find user with ID: " + userId);
        System.out.println("Current users: " + users);
        return users.get(userId);
    }


    public void buyShares(String userId, String outcome, int shares) {
        outcome = outcome.toUpperCase();
        User user = users.get(userId);

        if (user.getId() == null) {
            System.out.println("Transaction failed. User not found.");
            return;
        }

        System.out.println("User " + userId + " is buying " + shares + " " + outcome + " shares.");

        double cost = super.buyShares(outcome, shares);
        if (cost <= 0 || user.getBalance() < cost) {
            System.out.println("Transaction failed. Invalid purchase or insufficient balance.");
            return;
        }

        user.setBalance(user.getBalance() - cost);
        user.addShares(outcome, shares);
        System.out.println("User " + userId + " bought " + shares + " " + outcome + " shares for " + cost);
    }
    public double calculateCost(String outcome, int shares) {
        outcome = outcome.toUpperCase();
        double tempYesLiquidity = getYesLiquidity(); // Temporary variables to simulate the transaction
        double tempNoLiquidity = getNoLiquidity();
        double cost = 0;
        double k = tempYesLiquidity * tempNoLiquidity; // Constant product for liquidity

        for (int i = 0; i < shares; i++) {
            if ("YES".equalsIgnoreCase(outcome)) {
                tempYesLiquidity += 1;
                tempNoLiquidity = k / tempYesLiquidity;
            } else if ("NO".equalsIgnoreCase(outcome)) {
                tempNoLiquidity += 1;
                tempYesLiquidity = k / tempNoLiquidity;
            } else {
                return -1; // Invalid outcome
            }

            double pricePerShare = tempYesLiquidity / (tempYesLiquidity + tempNoLiquidity);
            if ("NO".equalsIgnoreCase(outcome)) {
                pricePerShare = tempNoLiquidity / (tempYesLiquidity + tempNoLiquidity);
            }

            cost += pricePerShare;
        }

        double fee = cost * 0.001; // Assuming a 0.1% fee, for example
        cost += fee;

        return cost;
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