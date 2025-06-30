package com.bookshop.service;

import com.bookshop.model.Book;
import com.bookshop.model.CartItem;
import com.bookshop.model.Customer;
import com.bookshop.repository.CartItemRepository;
import com.bookshop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private BookRepository bookRepo;

    public void addToCart(Book book, Customer customer) {
        CartItem item = new CartItem();
        item.setBook(book);
        item.setCustomer(customer);
        item.setQuantity(1);
        cartItemRepo.save(item);
    }

    public List<CartItem> getCartItems(Customer customer) {
        return cartItemRepo.findByCustomer(customer);
    }

    public void removeItem(Long id) {
        cartItemRepo.deleteById(id);
    }
}
