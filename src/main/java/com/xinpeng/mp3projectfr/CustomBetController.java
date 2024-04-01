package com.xinpeng.mp3projectfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
public class CustomBetController {

    private BetService betService;

    public CustomBetController(BetService betService) {
        this.betService = betService;
    }

    @GetMapping("/create")
    public String showCreateBetForm(Model model) {
        // Add an attribute to the model for form binding
        model.addAttribute("bet", new Bet());
        return "create-bet";
    }

    @PostMapping("/create")
    public String createBet(@ModelAttribute Bet bet) {
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
        return "redirect:/bets/list";
    }

    @GetMapping("/list")
    public String listBets(Model model) {
        model.addAttribute("bets", betService.getAllBets());
        return "list-bets";
    }

    // ResolveBet helper class to bind the resolve form data

    }


