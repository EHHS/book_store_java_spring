package com.bookshop.handler;

import com.bookshop.model.Customer;
import com.bookshop.repository.CustomerRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired private CustomerRepository customerRepo;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        String role = authentication.getAuthorities().stream()
                .findFirst().get().getAuthority();

        String username = ((User) authentication.getPrincipal()).getUsername();
        Customer customer = customerRepo.findByUsername(username);

        if (customer != null && Boolean.TRUE.equals(customer.getMfaEnabled())) {
            response.sendRedirect("/mfa/verify");
            return;
        }

        if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin/books");
        } else {
            response.sendRedirect("/books");
        }
    }
}
