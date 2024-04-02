package com.xinpeng.mp3projectfr;

import com.xinpeng.mp3projectfr.Bet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BetService {

    private List<Bet> bets = new ArrayList<>(); // Replacing the repository

    public Bet createBet(String name, String description) {
        Bet bet = new Bet(name, description);
        // Assuming a manual way of setting ID or not using ID at all
        bets.add(bet);
        return bet;
    }

    public List<Bet> getAllBets() {
        return bets;
    }

    public Bet resolveBet(Long betId, String winningOutcome) {
        for (Bet bet : bets) {
            if (bet.getId().equals(betId)) { // Assuming you're manually managing IDs
                bet.resolveBet(winningOutcome);
                return bet;
            }
        }
        return null; // No bet found with the given ID
    }

    public Bet createBet(Bet bet) {
        bets.add(bet);
        return bet;
    }

    // Additional methods for updating and deleting bets as needed
}
