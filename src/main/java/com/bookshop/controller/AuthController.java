package com.bookshop.controller;

import com.bookshop.model.Customer;
import com.bookshop.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.bookshop.service.AuditService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuditService auditService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("customer") Customer customer,
                                  BindingResult bindingResult,
                                  Model model) {
        if (customerRepo.findByUsername(customer.getUsername()) != null) {
            bindingResult.rejectValue("username", "duplicate", "Username already taken");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("customer", customer);
            return "register";
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole("CUSTOMER");
        customerRepo.save(customer);
        auditService.registrationCreated(customer.getUsername());
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
