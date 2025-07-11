package com.bookshop.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        String role = authentication.getAuthorities().stream()
                .findFirst().get().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin/books");
        } else {
            response.sendRedirect("/books");
        }
    }
}
