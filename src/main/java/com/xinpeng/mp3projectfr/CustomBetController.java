package com.xinpeng.mp3projectfr;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomBetController {

    private final BetService betService;
    private UnifiedBettingSystem unifiedBettingSystem;
    private UserService UserService;

    @Autowired
    public CustomBetController(BetService betService, UserService userService, UnifiedBettingSystem unifiedBettingSystem) {
        this.betService = betService;
        this.UserService = userService;
        System.out.println("f23eufwqhufq");

        System.out.println(userService.toString()) ;
        this.unifiedBettingSystem = unifiedBettingSystem;
    }

    @GetMapping("/create")
    public String showCreateBetForm(Model model) {
        model.addAttribute("bet", new UnifiedBettingSystem());
        return "create-bet";
    }

    @PostMapping("/create")
    public String createBet(@ModelAttribute UnifiedBettingSystem unifiedBettingSystem, HttpSession session, RedirectAttributes redirectAttrs) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        UnifiedBettingSystem createdBet = betService.createCustomBet(unifiedBettingSystem.getName(), unifiedBettingSystem.getDescription(), UserService);
        redirectAttrs.addFlashAttribute("betId", createdBet.getId());
        return "redirect:/bets/list";
    }


@GetMapping("/bets/view")
public String viewBets() {
    return "redirect:/bets/list"; // Redirect to the handler that shows the list of bets
}

    @PostMapping("/placeBet")
    public String placeBet(@RequestParam Long betId, @RequestParam String transactionType, @RequestParam String outcome, @RequestParam double amount, HttpSession session, RedirectAttributes redirectAttrs) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

       System.out.println(betService.getAllBets().toString());
        UnifiedBettingSystem bet = betService.getBetById(betId);
        bet.setUserService(UserService);
        if (bet == null) {
            redirectAttrs.addFlashAttribute("error", "Bet not found.");
            return "redirect:/";
        }

        try {
            if ("BUY".equalsIgnoreCase(transactionType)) {
                bet.buyShares(user.getId(), outcome, amount);
                System.out.println("User " + user.getId() + " bought " + amount + " shares for " + outcome + " at " + bet.getName() + " bet.");
            } else if ("SELL".equalsIgnoreCase(transactionType)) {
                bet.sellShares(user.getId(), outcome, (int) amount);
                System.out.println("User " + user.getId() + " sold " + amount + " shares for " + outcome + " at " + bet.getName() + " bet.");
            } else {
                throw new IllegalArgumentException("Invalid transaction type.");
            }

            redirectAttrs.addFlashAttribute("message", "Transaction successful!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("error", "Transaction failed: " + e.getMessage());
            System.out.println("Transaction failed: " + e.getMessage());
        }

        return "redirect:/bets/list";
    }

    @GetMapping("/bet/{betId}")
    public ModelAndView viewBet(@PathVariable Long betId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("bet_page");

        // Check if the user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/login"); // Redirect to the login page if the user is not logged in
        }


        UnifiedBettingSystem bet = betService.getBetById(betId);
        bet.setUserService(UserService);

        if (bet == null) {
            modelAndView.addObject("error", "The requested bet does not exist.");
            modelAndView.setViewName("error");
            return modelAndView;
        }

        modelAndView.addObject("bet", bet);
        modelAndView.addObject("balance", user.getBalance());
        modelAndView.addObject("yesPrice", bet.getPrice("YES"));
        modelAndView.addObject("noPrice", bet.getPrice("NO"));
        modelAndView.addObject("name", bet.getName());
        modelAndView.addObject("description", bet.getDescription());

        return modelAndView;
    }




    @PostMapping("/custombet/create")
    public String createCustomBet(@ModelAttribute("bet") UnifiedBettingSystem bet) {
        betService.createBet(bet);
        return "redirect:/bets/view";
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

    @GetMapping("/bets/list")
    public String listBets(Model model) {
        List<UnifiedBettingSystem> bets = betService.getAllBets(); // Fetch all bets from your service
        model.addAttribute("bets", bets); // Add the list of bets to the model
        return "list-bets"; // Return the list-bets view
    }
}
