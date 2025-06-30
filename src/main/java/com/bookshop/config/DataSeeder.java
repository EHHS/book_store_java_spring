package com.bookshop.config;

import com.bookshop.model.Admin;
import com.bookshop.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (adminRepo.findAll().isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("ehhs");
            admin.setPassword(encoder.encode("12345678"));
            admin.setRole("ADMIN");
            adminRepo.save(admin);
            System.out.println("Admin user 'ehhs' created.");
        }
    }
}
