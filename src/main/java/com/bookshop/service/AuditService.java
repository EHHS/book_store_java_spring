package com.bookshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger("AUDIT");

    public void registrationCreated(String username) {
        log.info("audit event=registration user={}", username);
    }

    public void orderPlaced(String username, int itemCount) {
        log.info("audit event=orderPlaced user={} items={}", username, itemCount);
    }
}


