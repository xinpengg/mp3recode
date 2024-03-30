package com.xinpeng.mp3projectfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BettingController {

    private final BettingService bettingService;

    @Autowired
    public BettingController(BettingService bettingService) {
        this.bettingService = bettingService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("yesLiquidity", bettingService.getYesLiquidity());
        modelAndView.addObject("noLiquidity", bettingService.getNoLiquidity());
        return modelAndView;
    }

    @PostMapping("/bet")
    public ModelAndView placeBet(@RequestParam String userId, @RequestParam String outcome, @RequestParam int shares) {
        System.out.println("Placing bet for user: " + userId + ", outcome: " + outcome + ", shares: " + shares);
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        try {
            if ("yes".equalsIgnoreCase(outcome)) {
                bettingService.buyShares(userId, "YES", shares);
            } else if ("no".equalsIgnoreCase(outcome)) {
                bettingService.buyShares(userId, "NO", shares);
            }
            modelAndView.addObject("message", "Bet placed successfully!");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }

}