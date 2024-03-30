package com.xinpeng.mp3projectfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String submitRegistrationForm(@RequestParam String username, @RequestParam double balance, Model model) {
        User user = new User(username, balance);
        userService.register(user); // Register the user
        model.addAttribute("user", user);
        return "redirect:/"; // Redirect to the home page or another confirmation page
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login";
    }
}
