package com.bookshop.repository;

import com.bookshop.model.Cart;
import com.bookshop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomer(Customer customer);
}

