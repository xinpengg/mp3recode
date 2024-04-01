package com.xinpeng.mp3projectfr;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BettingController {

    private final BettingService bettingService;
    private final UserService UserService;

    @Autowired
    public BettingController(BettingService bettingService, UserService userService) {
        this.bettingService = bettingService;
        UserService = userService;
        this.bettingService.setUserService(userService);
    }

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        // Check if user is logged in by looking for "user" attribute in session
        if (session.getAttribute("user") != null) {
            ModelAndView modelAndView = new ModelAndView("index");
            modelAndView.addObject("yesLiquidity", bettingService.getYesLiquidity());
            modelAndView.addObject("noLiquidity", bettingService.getNoLiquidity());
            return modelAndView; // Return "index" view if user is logged in
        } else {
            return new ModelAndView("redirect:/login"); // Redirect to login if not logged in
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Assuming you have a login.html in your template directory
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = UserService.findUserById(username);

        if (user != null && password.equals(user.getPassword())) {
            session.setAttribute("user", user); // Set user in session upon successful login
            return "redirect:/"; // Redirect to the home page
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            return "redirect:/login"; // Redirect back to login with error message
        }
    }


    @PostMapping("/bet")
    public ModelAndView placeBet(@RequestParam String userId, @RequestParam String outcome, @RequestParam int shares) {
        System.out.println("Placing bet for user: " + userId + ", outcome: " + outcome + ", shares: " + shares);

        ModelAndView modelAndView = new ModelAndView("redirect:/");

        if (!UserService.existsById(userId)) {
            modelAndView.addObject("error", "User must be registered to place bets.");
            return modelAndView;
        }

        User user = UserService.findUserById(userId);

        try {
            // Print all registered users for debugging
            System.out.println("Registered users: " + UserService.getAllUsers());

            // Check if the user has enough balance
            double cost = bettingService.calculateCost(outcome.toUpperCase(), shares);
            if (user == null) {
                System.out.println("User with ID " + userId + " is null (not found).");
                throw new IllegalArgumentException("User does not exist.");
            }

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