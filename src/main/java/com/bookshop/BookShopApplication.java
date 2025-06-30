package com.bookshop;

import com.bookshop.model.Book;
import com.bookshop.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookShopApplication.class, args);
    }
    @Bean
    public CommandLineRunner loadData(BookRepository bookRepository) {
        return (args) -> {
            if (bookRepository.count() == 0) {
                bookRepository.save(new Book("Clean Code", "Robert C. Martin","Programming", 45.00, 2020));
                bookRepository.save(new Book("Effective Java", "Joshua Bloch","Programming", 45.00, 2010));
                bookRepository.save(new Book("The Hobbit", "J.R.R. Tolkien","Programming", 45.00, 2000));
            }
        };
    }
}