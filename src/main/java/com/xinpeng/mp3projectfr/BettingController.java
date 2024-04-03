package com.xinpeng.mp3projectfr;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BettingController {

    private UnifiedBettingSystem unifiedBettingSystem;
    private UserService UserService;

    @Autowired
    public BettingController(UnifiedBettingSystem unifiedBettingSystem, UserService userService) {
        this.unifiedBettingSystem = unifiedBettingSystem;
        UserService = userService;
        this.unifiedBettingSystem.setUserService(userService);
    }
    @GetMapping("/")
    public String indexRedirect() {
        return "redirect:/bets/list";
    }



    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = UserService.findUserById(username);

        if (user != null && password.equals(user.getPassword())) {
            System.out.println(user.getId() + " logged in successfully!");
            session.setAttribute("user", user);

            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            System.out.println("Login failed.");
            return "redirect:/login";
        }
    }

    @PostMapping("/sell")
    public ModelAndView sellShares(@RequestParam String outcome, @RequestParam int shares, HttpSession session) {
        User user = (User) session.getAttribute("user");
        ModelAndView modelAndView = new ModelAndView("redirect:/");

        if (user == null || !UserService.existsById(user.getId())) {
            modelAndView.addObject("error", "User must be logged in and registered to sell shares.");
            return modelAndView;
        }

        try {
            unifiedBettingSystem.sellShares(user.getId(), outcome, shares);
            modelAndView.addObject("message", "Shares sold successfully!");
        } catch (IllegalArgumentException e) {
            modelAndView.addObject("error", e.getMessage());
        }

        return modelAndView;
    }


    @PostMapping("/bet")
    public ModelAndView placeBet(@RequestParam String transactionType, @RequestParam String outcome, @RequestParam int amount, HttpSession session) {
        User user = (User) session.getAttribute("user");

        System.out.println("Placing bet for user: " + user.getId() + ", transaction type: " + transactionType + ", outcome: " + outcome + ", amount: " + amount);
        ModelAndView modelAndView = new ModelAndView("redirect:/");

        if (!UserService.existsById(user.getId())) {
            modelAndView.addObject("error", "User must be registered to place bets.");
            return modelAndView;
        }

        try {
            if (user == null) {
                throw new IllegalArgumentException("User does not exist.");
            }

            if (amount > user.getBalance()) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            if ("BUY".equalsIgnoreCase(transactionType)) {
                if ("yes".equalsIgnoreCase(outcome)) {
                    unifiedBettingSystem.buyShares(user.getId(), "YES", amount);
                } else if ("no".equalsIgnoreCase(outcome)) {
                    unifiedBettingSystem.buyShares(user.getId(), "NO", amount);
                }
            } else if ("SELL".equalsIgnoreCase(transactionType)) {
                if ("yes".equalsIgnoreCase(outcome)) {
                    unifiedBettingSystem.sellShares(user.getId(), "YES", amount);
                } else if ("no".equalsIgnoreCase(outcome)) {
                    unifiedBettingSystem.sellShares(user.getId(), "NO", amount);
                }
            }

            // Update the user's balance in the session after the transaction
            user = UserService.findUserById(user.getId()); // Assuming UserService.findById returns the updated User object
            session.setAttribute("user", user);

            modelAndView.addObject("message", "Transaction processed successfully!");
        } catch (IllegalArgumentException e) {
            modelAndView.addObject("error", e.getMessage());
        }

        return modelAndView;
    }
}