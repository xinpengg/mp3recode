package com.xinpeng.mp3projectfr;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class BettingService extends Market {
    private Market market;
    public Map<String, User> users;

    private   UserService userService;

    public BettingService() {
        super(10000,10000);
        this.users = new HashMap<>();
    }
    public BettingService(UserService userService) {
        super(10000, 10000);
        this.userService = userService;
        this.users = new HashMap<>();

    }

    public BettingService(int someValue) {
super (someValue, someValue);
        this.users = new HashMap<>();
    }

    public BettingService(Market market, UserService userService) {
        this(userService);
        this.market = market;

    }

    public BettingService(int yesLiquidity, int noLiquidity) {
        super(yesLiquidity, noLiquidity);
        this.users = new HashMap<>();
    }

    public void registerUser(String userId, String paswsword) {
        users.put(userId, new User(userId, paswsword));
        System.out.println("User " + userId + " registered with password: " + paswsword);
    }

    public void sellShares(String userId, String outcome, double amount) {
        outcome = outcome.toUpperCase();
        User user = userService.findUserById(userId);

        if (user == null) {
            System.out.println("Transaction failed. User not found.");
            return;
        }

        int shares = user.getShares(outcome); // Get the total number of shares the user has for the outcome

        if (shares <= 0) {
            System.out.println("Transaction failed. No shares to sell.");
            return;
        }
        if (shares >= amount ) {
            double cost = super.sellShares(outcome, (int) amount); // Sell all the shares
            user.setBalance(user.getBalance() + cost);
            user.removeShares(outcome, (int) amount);
            userService.save(user);
            System.out.println("User " + userId + " sold " + shares + " " + outcome + " shares for " + cost);

        }


    }
    public User findUserById(String userId) {
        System.out.println("Trying to find user with ID: " + userId);
        System.out.println("Current users: " + users);
        return users.get(userId);
    }


    public void buyShares(String userId, String outcome, double amount) {
        outcome = outcome.toUpperCase();
        User user = userService.findUserById(userId); // Retrieve user from UserService

        if (user == null) {
            System.out.println("Transaction failed. User not found.");
            return;
        }

        double pricePerShare = outcome.equals("YES") ? getYesPrice() : getNoPrice();
        int shares = 0;
        double cost = 0;

        while (cost + pricePerShare <= amount && user.getBalance() - cost >= pricePerShare) {
            shares++;
            cost += pricePerShare;
        }

        System.out.println("User " + userId + " is buying " + shares + " " + outcome + " shares for " + cost + ".");

        if (cost <= 0 || user.getBalance() < cost) {
            System.out.println("Transaction failed. Invalid purchase or insufficient balance.");
            return;
        }

        super.buyShares(outcome, shares);

        user.setBalance(user.getBalance() - cost);
        System.out.println(user.getBalance());
        user.addShares(outcome, shares);
        userService.save(user);

        System.out.println("User " + userId + " bought " + shares + " " + outcome + " shares for " + cost);
    }


    protected void setUserService(UserService service) {
        	        this.userService = service;
        	    }
    public double calculateCost(String outcome, int shares) {
        outcome = outcome.toUpperCase();
        double tempYesLiquidity = getYesLiquidity();
        double tempNoLiquidity = getNoLiquidity();
        double cost = 0;
        double k = tempYesLiquidity * tempNoLiquidity;

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





    public void printTotalShares(String userId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("User " + userId + " has " + user.getTotalShares() + " total shares.");
    }

}