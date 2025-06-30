package com.bookshop.controller;

import com.bookshop.model.Book;
import com.bookshop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepo;

    @GetMapping("/books")
    public String viewBooksForCustomer(Model model) {
        List<Book> books = bookRepo.findAll();
        model.addAttribute("books", books);
        return "book_list";
    }
}
