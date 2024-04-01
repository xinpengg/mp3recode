package com.xinpeng.mp3projectfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetService {

    private final BetRepository betRepository;

    @Autowired
    public BetService(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    public Bet createBet(String name, String outcome, double description) {
        Bet bet = new Bet(name, outcome, description);
        return betRepository.save(bet);
    }
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }

    public Bet resolveBet(Long betId, String winningOutcome) {
        Bet bet = betRepository.findById(betId).orElse(null); // Find the bet by ID
        if (bet != null) {
            bet.resolveBet(winningOutcome); // Resolve the bet using the provided outcome
            return betRepository.save(bet); // Save the changes to the bet
        }
        return null;
    }

    public Bet createBet(Bet bet) {
        return betRepository.save(bet);

    }
}

