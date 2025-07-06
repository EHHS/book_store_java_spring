package com.bookshop.controller;

import com.bookshop.model.Customer;
import com.bookshop.model.CartItem;
import com.bookshop.service.CartService;
import com.bookshop.repository.CustomerRepository;
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
        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal User user,
                             @RequestParam String cardNumber,
                             @RequestParam String cardName,
                             @RequestParam String expiryDate,
                             @RequestParam String cvv,
                             Model model) {

        Customer customer = getCustomer(user);
        List<CartItem> items = cartService.getCartItems(customer);

        if (items.isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "cart";
        }

        cartService.clearCart(customer);

        String last4 = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : "****";
        model.addAttribute("message", "Order placed successfully with card ending in " + last4);
        return "order_success";
    }

}
