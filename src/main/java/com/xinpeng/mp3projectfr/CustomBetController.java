package com.xinpeng.mp3projectfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomBetController {

    private final BetService betService;

    @Autowired
    public CustomBetController(BetService betService) {
        this.betService = betService;
    }

    @GetMapping("/create")
    public String showCreateBetForm(Model model) {
        model.addAttribute("bet", new Bet());
        return "create-bet";
    }

    @PostMapping("/create")
    public String createBet(@ModelAttribute Bet bet) {
        betService.createBet(bet);
        return "redirect:/bets/list";
    }

    @PostMapping("/custombet/create")
    public String createCustomBet(@ModelAttribute("bet") Bet bet) {
        betService.createBet(bet);
        return "redirect:/bets/list";
    }

    @GetMapping("/resolve")
    public String showResolveBetForm(Model model) {
        model.addAttribute("resolveBet", new ResolveBet());
        return "resolve-bet";
    }


    @PostMapping("/resolve")
    public String resolveBet(@ModelAttribute ResolveBet resolveBet) {
        betService.resolveBet(resolveBet.getBetId(), resolveBet.getWinningOutcome());
        return "redirect:/bets/list"; // Ensure this matches the GET mapping
    }

    @GetMapping("/bets/list") // Make sure this matches the redirect path
    public String listBets(Model model) {
        model.addAttribute("bets", betService.getAllBets());
        System.out.println("Bets: " + betService.getAllBets());
        return "list-bets";
    }
}
