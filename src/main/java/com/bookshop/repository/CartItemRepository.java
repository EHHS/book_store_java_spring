package com.bookshop.repository;

import com.bookshop.model.CartItem;
import com.bookshop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(Customer customer);
    void deleteByCustomer(Customer customer);
}
