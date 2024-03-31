package com.xinpeng.mp3projectfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BettingController {

    private final BettingService bettingService;
    private final UserService UserService;

    @Autowired
    public BettingController(BettingService bettingService, UserService userService) {
        this.bettingService = bettingService;
        UserService = userService;
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
        if (!UserService.existsById(userId)) {
            modelAndView.addObject("error", "User must be registered to place bets.");
            return modelAndView;
        }

            User user = bettingService.findUserById(userId);
        try {
            // Check if the user exists
            System.out.println("Registered users: " + UserService.getAllUsers());
            System.out.println("Available users before placing a bet: " + UserService.getAllUsers());
            if (user == null) {
                System.out.println("User with ID " + userId + " is null (not found).");
            } else {
                System.out.println("User found: " + user);
            }

            if (user == null) {
                throw new IllegalArgumentException("User does not exist.");
            }

            // Check if the user has enough balance
            double cost = bettingService.calculateCost(outcome.toUpperCase(), shares);
            if (cost > user.getBalance()) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            // Place the bet if the outcome is valid and the user has enough balance
            if ("yes".equalsIgnoreCase(outcome)) {
                bettingService.buyShares(userId, "YES", shares);
            } else if ("no".equalsIgnoreCase(outcome)) {
                bettingService.buyShares(userId, "NO", shares);
            }
            modelAndView.addObject("message", "Bet placed successfully!");
        } catch (IllegalArgumentException e) {
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }


}