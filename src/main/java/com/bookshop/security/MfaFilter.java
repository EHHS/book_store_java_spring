package com.bookshop.security;

import com.bookshop.model.Customer;
import com.bookshop.repository.CustomerRepository;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MfaFilter extends OncePerRequestFilter {

    @Autowired private CustomerRepository customerRepo;

    @Override
    protected void doFilterInternal(@NonNull jakarta.servlet.http.HttpServletRequest request,
                                    @NonNull jakarta.servlet.http.HttpServletResponse response,
                                    @NonNull jakarta.servlet.FilterChain filterChain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User user) {
            String path = request.getRequestURI();
            // allow MFA pages and logout without enforcement
            if (!path.startsWith("/mfa") && !path.startsWith("/logout") && !path.startsWith("/css") && !path.startsWith("/images")) {
                Customer customer = customerRepo.findByUsername(user.getUsername());
                if (customer != null && Boolean.TRUE.equals(customer.getMfaEnabled())) {
                    Object verified = request.getSession().getAttribute("MFA_VERIFIED");
                    if (!(verified instanceof Boolean) || !((Boolean) verified)) {
                        response.sendRedirect("/mfa/verify");
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}


