package com.xinpeng.mp3projectfr;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.concurrent.atomic.AtomicLong;

public class Bet extends BettingService {

    private static final AtomicLong ID_COUNTER = new AtomicLong(0);


    private Long id;

    private String name;
    private String outcome;
    private String description;
    private String odds;



    public Bet() {
            this.id = ID_COUNTER.incrementAndGet(); // Ensuring that this line is being executed

    }

    public Bet(String name, String description) {
        this();
        this.name = name;
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

    @Override
    public String toString() {
        return "Bet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", outcome='" + outcome + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
    public String getName() {
        return name;
    }
    public String getOutcome() {
        return outcome;
    }
    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public String getOdds() {
        return odds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
