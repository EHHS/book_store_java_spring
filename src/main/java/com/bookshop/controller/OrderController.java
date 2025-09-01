package com.bookshop.controller;

import com.bookshop.model.Customer;
import com.bookshop.model.CartItem;
import com.bookshop.service.CartService;
import com.bookshop.service.AuditService;
import com.bookshop.repository.CustomerRepository;
import com.bookshop.web.PaymentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired private CartService cartService;
    @Autowired private CustomerRepository customerRepo;
    @Autowired private AuditService auditService;

    private Customer getCustomer(User user) {
        return customerRepo.findByUsername(user.getUsername());
    }

    @GetMapping("/checkout")
    public String checkoutPage(@AuthenticationPrincipal User user, Model model) {
        Customer customer = getCustomer(user);
        List<CartItem> cartItems = cartService.getCartItems(customer);

        if (cartItems.isEmpty()) {
            model.addAttribute("error", "Your cart is empty. Add items before placing an order.");
            return "cart";
        }

        model.addAttribute("total", cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        model.addAttribute("payment", new com.bookshop.web.PaymentRequest());
        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal User user,
                             @Valid @ModelAttribute("payment") PaymentRequest payment,
                             org.springframework.validation.BindingResult bindingResult,
                             Model model) {

        Customer customer = getCustomer(user);
        List<CartItem> items = cartService.getCartItems(customer);

        if (items.isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "cart";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("total", items.stream()
                    .map(CartItem::getTotalPrice)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
            return "checkout";
        }

        cartService.clearCart(customer);

        String digits = payment.getCardNumber().replaceAll("\\s+", "");
        String last4 = digits.length() >= 4 ? digits.substring(digits.length() - 4) : "****";
        auditService.orderPlaced(customer.getUsername(), items.size());
        model.addAttribute("message", "Order placed successfully with card ending in " + last4);
        return "order_success";
    }

}
