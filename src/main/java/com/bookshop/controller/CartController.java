package com.bookshop.controller;

import com.bookshop.model.Book;
import com.bookshop.model.CartItem;
import com.bookshop.model.Customer;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.CustomerRepository;
import com.bookshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private BookRepository bookRepo;
    @Autowired private CustomerRepository customerRepo;

    private Customer getCustomer(User user) {
        return customerRepo.findByUsername(user.getUsername());
    }

    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, @AuthenticationPrincipal User user) {
        Book book = bookRepo.findById(bookId).orElseThrow();
        cartService.addToCart(book, getCustomer(user));
        return "redirect:/books";
    }

    @GetMapping
    public String viewCart(@AuthenticationPrincipal User user, Model model) {
        List<CartItem> items = cartService.getCartItems(getCustomer(user));
        model.addAttribute("cartItems", items);
        return "cart";
    }

    @PostMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
        return "redirect:/cart";
    }
}
