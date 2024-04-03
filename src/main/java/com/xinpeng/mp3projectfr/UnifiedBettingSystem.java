package com.xinpeng.mp3projectfr;

import com.xinpeng.mp3projectfr.UserService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UnifiedBettingSystem {
    private static final AtomicLong ID_COUNTER = new AtomicLong(0);
    private String resolutionPasswordHash;
    private Long id;
    private String name;
    private String outcome;
    private String description;
    private double yesLiquidity;
    private double noLiquidity;
    private boolean resolved = false;

    private Map<String, User> users = new HashMap<>();
    @Autowired
    private UserService userService;

    public UnifiedBettingSystem() {
        this.id = ID_COUNTER.incrementAndGet();
        this.yesLiquidity = 5000;
        this.noLiquidity = 5000;
    }

    public UnifiedBettingSystem(String name, String description) {
        this();
        this.id = ID_COUNTER.incrementAndGet();
        this.name = name;
        this.description = description;
    }

    public UnifiedBettingSystem(double yesLiquidity, double noLiquidity) {
        this();
        this.id = ID_COUNTER.incrementAndGet();
        this.yesLiquidity = yesLiquidity;
        this.noLiquidity = noLiquidity;
    }

    public void registerUser(String userId, String password) {
        this.id = ID_COUNTER.incrementAndGet();
        users.put(userId, new User(userId, password));
        System.out.println("User " + userId + " registered with password: " + password);
    }

    public void buyShares(String userId, String outcome, double amount) {
        outcome = outcome.toUpperCase();
        User user = userService.findUserById(userId);

        if (user == null) {
            System.out.println("Transaction failed. User not found.");
            return;
        }

        double pricePerShare = outcome.equals("YES") ? getPrice("YES") : getPrice("NO");
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

        double k = yesLiquidity * noLiquidity;

        for (int i = 0; i < shares; i++) {
            if ("YES".equalsIgnoreCase(outcome)) {
                yesLiquidity += 1;
                noLiquidity = k / yesLiquidity;
            } else if ("NO".equalsIgnoreCase(outcome)) {
                noLiquidity += 1;
                yesLiquidity = k / noLiquidity;
            } else {
                return;
            }

            pricePerShare = getPrice(outcome);
        }

        double fee = cost * 0.001;
        cost += fee;

        user.setBalance(user.getBalance() - cost);
        System.out.println(user.getBalance());
        user.addShares(outcome, shares);
        userService.save(user);

        System.out.println("User " + userId + " bought " + shares + " " + outcome + " shares for " + cost);
    }


    public User findUserById(String userId) {
        System.out.println("Trying to find user with ID: " + userId);
        System.out.println("Current users: " + users);
        return users.get(userId);
    }


    public void sellShares(String userId, String outcome, double amount) {
        outcome = outcome.toUpperCase();
        User user = userService.findUserById(userId);

        if (user == null) {
            System.out.println("Transaction failed. User not found.");
            return;
        }

        int shares = user.getShares(outcome);

        if (shares <= 0) {
            System.out.println("Transaction failed. No shares to sell.");
            return;
        }
        if (shares >= amount ) {
            double cost = 0;
            double k = yesLiquidity * noLiquidity;

            for (int i = 0; i < amount; i++) {
                if ("YES".equalsIgnoreCase(outcome)) {
                    if (yesLiquidity <= 1) {
                        System.out.println("Transaction failed. Not enough liquidity to sell.");
                        return;
                    }
                    yesLiquidity -= 1;
                    noLiquidity = k / yesLiquidity;
                } else if ("NO".equalsIgnoreCase(outcome)) {
                    if (noLiquidity <= 1) {
                        System.out.println("Transaction failed. Not enough liquidity to sell.");
                        return;
                    }
                    noLiquidity -= 1;
                    yesLiquidity = k / noLiquidity;
                } else {
                    System.out.println("Transaction failed. Invalid outcome.");
                    return;
                }

                double pricePerShare = getPrice(outcome);
                cost += pricePerShare;
            }

            double fee = cost * 0.001;
            cost -= fee;

            user.setBalance(user.getBalance() + cost);
            user.removeShares(outcome, (int) amount);
            userService.save(user);
            System.out.println("User " + userId + " sold " + shares + " " + outcome + " shares for " + cost);
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
    public double getPrice(String outcome) {
        double totalLiquidity = yesLiquidity + noLiquidity;
        return "YES".equalsIgnoreCase(outcome) ? yesLiquidity / totalLiquidity : noLiquidity / totalLiquidity;
    }

    public double calculateCost(String outcome, int shares) {
        double cost = 0;
        double tempYesLiquidity = yesLiquidity;
        double tempNoLiquidity = noLiquidity;
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

        return cost;
    }
    public void resolveBet(String winningOutcome) {
        for (User user : users.values()) {
            int winningShares = user.getShares(winningOutcome);
            int losingShares = user.getShares(winningOutcome.equals("YES") ? "NO" : "YES");

            double profit = winningShares;
            user.addBalance(profit);

            if (winningShares > 0) {
                System.out.println("User " + user.getId() + " won " + profit + " from " + winningShares + " " + winningOutcome + " shares.");
            }

            user.removeShares("YES", user.getShares("YES"));
            user.removeShares("NO", user.getShares("NO"));
            this.outcome = winningOutcome;
            this.resolved = true;
        }
    }

    private void adjustLiquidityAfterSelling(String outcome, int shares) {
        double k = yesLiquidity * noLiquidity;
        if ("YES".equalsIgnoreCase(outcome) && yesLiquidity > shares) {
            yesLiquidity -= shares;
        } else if ("NO".equalsIgnoreCase(outcome) && noLiquidity > shares) {
            noLiquidity -= shares;
        }
        yesLiquidity = k / noLiquidity;
    }

    private double calculatePricePerShare(String outcome) {
        double totalLiquidity = yesLiquidity + noLiquidity;
        return "YES".equalsIgnoreCase(outcome) ? yesLiquidity / totalLiquidity : noLiquidity / totalLiquidity;
    }
    public void updatePrices(double totalLiquidity) {
        yesLiquidity = totalLiquidity * (yesLiquidity / (yesLiquidity + noLiquidity));
        noLiquidity = totalLiquidity * (noLiquidity / (yesLiquidity + noLiquidity));
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
    public void setResolutionPasswordHash(String creatorUserId) {
        this.resolutionPasswordHash = creatorUserId;
    }
    public String getResolutionPasswordHash() {
        return resolutionPasswordHash;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }





    public double getYesLiquidity() {
        return yesLiquidity;
    }

    public void setYesLiquidity(double yesLiquidity) {
        this.yesLiquidity = yesLiquidity;
    }

    public double getNoLiquidity() {
        return noLiquidity;
    }

    public void setNoLiquidity(double noLiquidity) {
        this.noLiquidity = noLiquidity;
    }
    public String toString() {
        return "UnifiedBettingSystem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", outcome='" + outcome + '\'' +
                ", description='" + description + '\'' +
                ", " + '\'' +
                ", yesLiquidity=" + yesLiquidity +
                ", noLiquidity=" + noLiquidity +
                '}';
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }




}