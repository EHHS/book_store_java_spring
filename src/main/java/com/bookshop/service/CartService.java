package com.bookshop.service;

import com.bookshop.model.Book;
import com.bookshop.model.Cart;
import com.bookshop.model.CartItem;
import com.bookshop.model.Customer;
import com.bookshop.repository.CartItemRepository;
import com.bookshop.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    @Autowired private CartItemRepository cartItemRepo;
    @Autowired private CartRepository cartRepo;

    public void addToCart(Book book, Customer customer, int quantity) {
        Cart cart = cartRepo.findByCustomer(customer);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            cartRepo.save(cart);
        }

        List<CartItem> items = cartItemRepo.findByCustomer(customer);
        for (CartItem item : items) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                cartItemRepo.save(item);
                return;
            }
        }

        CartItem newItem = new CartItem();
        newItem.setBook(book);
        newItem.setQuantity(quantity);
        newItem.setCustomer(customer);
        newItem.setCart(cart);
        cartItemRepo.save(newItem);
    }

    public List<CartItem> getCartItems(Customer customer) {
        return cartItemRepo.findByCustomer(customer);
    }

    public void removeItem(Long itemId) {
        CartItem item = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepo.save(item);
        } else {
            cartItemRepo.delete(item);
        }
    }

    @Transactional
    public void clearCart(Customer customer) {
        cartItemRepo.deleteByCustomer(customer);
    }
}
