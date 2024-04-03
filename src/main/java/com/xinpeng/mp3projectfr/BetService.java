package com.xinpeng.mp3projectfr;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BetService {

    private List<UnifiedBettingSystem> bets = new ArrayList<>();
    private AtomicLong betIdGenerator = new AtomicLong();


    public List<UnifiedBettingSystem> getAllBets() {
        return bets;
    }

    public UnifiedBettingSystem resolveBet(Long betId, String winningOutcome) {
        for (UnifiedBettingSystem bet : bets) {
            if (bet.getId().equals(betId)) {
                bet.resolveBet(winningOutcome);
                return bet;
            }
        }
        return null;
    }
    public UnifiedBettingSystem createCustomBet(String name, String description, UserService userService) {
        UnifiedBettingSystem newBet = new UnifiedBettingSystem( name, description);
        newBet.setId(generateUniqueId());
        System.out.println(userService.toString());
        newBet.setUserService(userService);
        bets.add(newBet);
        return newBet;
    }
    private Long generateUniqueId() {
        return betIdGenerator.incrementAndGet();
    }


    public UnifiedBettingSystem getBetById(Long betId) {
        return bets.stream()
                .filter(bet -> bet.getId().equals(betId))
                .findFirst()
                .orElse(null);
    }



    public UnifiedBettingSystem createBet(UnifiedBettingSystem bet) {
        bets.add(bet);
        return bet;
    }

    public void save(UnifiedBettingSystem bet) {
        bets.add(bet);
    }

}
