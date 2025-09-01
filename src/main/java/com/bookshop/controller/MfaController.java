package com.bookshop.controller;

import com.bookshop.model.Customer;
import com.bookshop.repository.CustomerRepository;
import com.bookshop.service.MfaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mfa")
public class MfaController {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private MfaService mfaService;

    private Customer getCustomer(User user) {
        return customerRepo.findByUsername(user.getUsername());
    }

    @GetMapping("/setup")
    public String setup(@AuthenticationPrincipal User user, Model model) {
        Customer customer = getCustomer(user);
        if (Boolean.TRUE.equals(customer.getMfaEnabled())) return "redirect:/books";

        String secret = mfaService.generateSecret();
        customer.setMfaSecret(secret);
        customerRepo.save(customer);

        String otpUrl = mfaService.buildOtpAuthUrl("Bookstore", customer.getUsername(), secret);
        model.addAttribute("otpAuthUrl", otpUrl);
        model.addAttribute("secret", secret);
        return "mfa_setup";
    }

    @PostMapping("/enable")
    public String enable(@AuthenticationPrincipal User user, @RequestParam int code, Model model) {
        Customer customer = getCustomer(user);
        if (customer.getMfaSecret() == null) return "redirect:/mfa/setup";
        if (mfaService.verifyCode(customer.getMfaSecret(), code)) {
            customer.setMfaEnabled(true);
            customerRepo.save(customer);
            return "redirect:/books";
        }
        model.addAttribute("error", "Invalid code. Try again.");
        String otpUrl = mfaService.buildOtpAuthUrl("Bookstore", customer.getUsername(), customer.getMfaSecret());
        model.addAttribute("otpAuthUrl", otpUrl);
        model.addAttribute("secret", customer.getMfaSecret());
        return "mfa_setup";
    }

    @GetMapping("/verify")
    public String verifyPage() { return "mfa_verify"; }

    @PostMapping("/verify")
    public String verify(@AuthenticationPrincipal User user, @RequestParam int code, jakarta.servlet.http.HttpServletRequest request, Model model) {
        Customer customer = getCustomer(user);
        if (!Boolean.TRUE.equals(customer.getMfaEnabled())) return "redirect:/books";
        if (mfaService.verifyCode(customer.getMfaSecret(), code)) {
            request.getSession().setAttribute("MFA_VERIFIED", true);
            return "redirect:/books";
        }
        model.addAttribute("error", "Invalid code. Try again.");
        return "mfa_verify";
    }
}


