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
    public String showCreateBetForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("bet", new UnifiedBettingSystem());
        return "create-bet";
    }

    @PostMapping("/create")
    public String createBet(@ModelAttribute UnifiedBettingSystem unifiedBettingSystem, @RequestParam String resolutionPassword, HttpSession session, RedirectAttributes redirectAttrs) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // Hash the resolution password and store it
        String resolutionPasswordHash = resolutionPassword;
        unifiedBettingSystem.setResolutionPasswordHash(user.getId());
        unifiedBettingSystem.setResolutionPasswordHash(resolutionPasswordHash);
        System.out.println("test" + resolutionPasswordHash);
        UnifiedBettingSystem createdBet = betService.createCustomBet(unifiedBettingSystem.getName(), unifiedBettingSystem.getDescription(), UserService);
        redirectAttrs.addFlashAttribute("betId", createdBet.getId());
        return "redirect:/bets/list";
    }


@GetMapping("/bets/view")
public String viewBets() {
    return "redirect:/bets/list";
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
                user = UserService.findUserById(user.getId());
                session.setAttribute("user", user);
                System.out.println("User " + user.getId() + " bought " + amount + " shares for " + outcome + " at " + bet.getName() + " bet.");
            } else if ("SELL".equalsIgnoreCase(transactionType)) {
                bet.sellShares(user.getId(), outcome, (int) amount);
                user = UserService.findUserById(user.getId());
                session.setAttribute("user", user);
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
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        UnifiedBettingSystem bet = betService.getBetById(betId);
        if (bet == null) {
            modelAndView.addObject("error", "The requested bet does not exist.");
            modelAndView.setViewName("error");
            return modelAndView;
        }

        // Debugging
        System.out.println("Session User ID: " + user.getId());

        modelAndView.addObject("bet", bet);
        modelAndView.addObject("balance", user.getBalance());
        modelAndView.addObject("yesPrice", bet.getPrice("YES"));
        modelAndView.addObject("noPrice", bet.getPrice("NO"));
        modelAndView.addObject("name", bet.getName());
        modelAndView.addObject("description", bet.getDescription());
        modelAndView.addObject("yesShares", user.getShares("YES"));
        modelAndView.addObject("noShares", user.getShares("NO"));


        return modelAndView;
    }




    @PostMapping("/custombet/create")
    public String createCustomBet(@ModelAttribute("bet") UnifiedBettingSystem bet) {
        betService.createBet(bet);
        return "redirect:/bets/view";
    }


    @GetMapping("/bets/{betId}/resolve")
    public String showResolveBetForm(@PathVariable Long betId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        UnifiedBettingSystem bet = betService.getBetById(betId);
        if (bet == null) {
            model.addAttribute("error", "The requested bet does not exist.");
            return "error";
        }
        model.addAttribute("betId", betId);
        return "resolve-bet";
    }
    @PostMapping("/bets/{betId}/resolve")
    public String resolveBet(@PathVariable Long betId,
                             @RequestParam String winningOutcome,
                             @RequestParam String resolutionPassword,
                             HttpSession session,
                             RedirectAttributes redirectAttrs) {

        UnifiedBettingSystem bet = betService.getBetById(betId);

        if (bet == null) {
            redirectAttrs.addFlashAttribute("error", "Bet not found.");
            return "redirect:/bets/list";
        }

        if (resolutionPassword.equals(bet.getResolutionPasswordHash())) {
            redirectAttrs.addFlashAttribute("error", "Incorrect resolution password.");
            return "redirect:/bets/" + betId + "/resolve";
        }

        betService.resolveBet(betId, winningOutcome);

        redirectAttrs.addFlashAttribute("message", "Bet resolved successfully with outcome: " + winningOutcome);
        System.out.println("Bet resolved successfully with outcome: " + winningOutcome);

        return "redirect:/bets/view";
    }


    @GetMapping("/bets/list")
    public String listBets(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<UnifiedBettingSystem> bets = betService.getActiveBets();
        model.addAttribute("bets", bets);
        return "list-bets";
    }
}
