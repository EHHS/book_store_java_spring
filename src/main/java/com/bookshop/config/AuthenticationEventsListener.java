package com.bookshop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventsListener {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationEventsListener.class);

    @EventListener
    public void onSuccess(InteractiveAuthenticationSuccessEvent event) {
        String user = event.getAuthentication().getName();
        log.info("auth success user={}", user);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String user = event.getAuthentication() != null ? event.getAuthentication().getName() : "unknown";
        String reason = event.getException() != null ? event.getException().getClass().getSimpleName() : "unknown";
        log.warn("auth failure user={} reason={}", user, reason);
    }
}


