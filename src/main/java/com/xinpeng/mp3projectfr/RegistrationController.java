package com.xinpeng.mp3projectfr;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String submitRegistrationForm(@ModelAttribute("user") User user, Model model) {
        boolean userExists = userService.existsById(user.getId());
        if (userExists) {
            model.addAttribute("error", "Username already exists. Choose a different username.");
            return "register";
        }

        user.setBalance(1000.0);

        userService.register(user);

        model.addAttribute("message", "Registration successful!");

        return "redirect:/login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    public static boolean userIsLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated and not anonymous
        return authentication != null &&
                authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser");
    }



    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/registration-success")
    public String registrationSuccess() {
        // Show a success message or page
        return "registration-success"; // A new HTML page that shows registration was successful
    }



}
