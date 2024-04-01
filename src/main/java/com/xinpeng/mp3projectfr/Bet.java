package com.xinpeng.mp3projectfr;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Bet  extends BettingService{
    private String name;
    private String outcome;
    private double description;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    public Bet() {
        super();
    }
    public Bet(String name, String outcome, double description) {
        this.name = name;
        this.outcome = outcome;
        this.description = description;
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

    public String getName() {
        return name;
    }
    public String getOutcome() {
        return outcome;
    }
    public double getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
