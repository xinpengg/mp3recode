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
            double balance = ((User) session.getAttribute("user")).getBalance();
            modelAndView.addObject("balance", balance);
            double yesPrice = bettingService.getYesPrice();
            double noPrice = bettingService.getNoPrice();
            modelAndView.addObject("yesPrice", yesPrice);
            modelAndView.addObject("noPrice", noPrice);
            modelAndView.addObject("yesLiquidity", bettingService.getYesLiquidity());
            modelAndView.addObject("noLiquidity", bettingService.getNoLiquidity());
            return modelAndView;
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
            System.out.println(user.getId() + " logged in successfully!");
            session.setAttribute("user", user);

            return "redirect:/" ;
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            System.out.println("Login failed.");
            return "redirect:/login";
        }
    }


    @PostMapping("/bet")
    public ModelAndView placeBet( @RequestParam String outcome, @RequestParam int amount, HttpSession session) {
        User user = (User) session.getAttribute("user");

        System.out.println("Placing bet for user: " + user.getId() + ", outcome: " + outcome + ", amount: " + amount);
        ModelAndView modelAndView = new ModelAndView( "redirect:/");



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

            if ("yes".equalsIgnoreCase(outcome)) {
                bettingService.buyShares(user.getId(), "YES", amount );
            } else if ("no".equalsIgnoreCase(outcome)) {
                bettingService.buyShares(user.getId(), "NO", amount);
            }
            modelAndView.addObject("message", "Bet placed successfully!");
        } catch (IllegalArgumentException e) {
            modelAndView.addObject("error", e.getMessage());
        }

        return modelAndView;
    }



}