package com.example.JDBC_project.controller;

import com.example.JDBC_project.model.Book;
import com.example.JDBC_project.service.BookService;
import com.example.JDBC_project.service.ManService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final ManService manService;

    private static final Logger logger = LogManager.getLogger(BookController.class);

    @Autowired
    public BookController(BookService bookService, ManService manService) {
        this.bookService = bookService;
        this.manService = manService;
    }

    @GetMapping
    public String getBooksPage(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books_package/books";
    }

    @GetMapping("/{id}")
    public String getBookPage(Model model, @PathVariable("id") int id) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        if (book.getManId() == 0) {
            model.addAttribute("people", manService.getAllPeople());
        }
        else {
            model.addAttribute("man", manService.findById(book.getManId()));
        }
        return "books_package/book";
    }

    @PostMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @RequestParam("select") int manId) {
        bookService.assignBook(id, manId);
        logger.info("Человек с id: {} взял книгу с id: {}", manId, id);
        return "redirect:/books/{id}";
    }

    @PostMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        bookService.doFreeBook(id);
        logger.info("Книга с id: {} теперь свободна", id);
        return "redirect:/books/{id}";
    }

    @GetMapping("/new")
    public String getCreateBookPage(@ModelAttribute("book") Book book) {
        return "books_package/book_new";
    }

    @PostMapping("/new")
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Введи неккоректные данные при создании книги");
            return "books_package/book_new";
        }
        bookService.saveBook(book);
        logger.info("Книга была успешно создана");
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String getEditBookPage(@ModelAttribute("book") Book book, @PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findBookById(id));
        return "books_package/book_edit";
    }

    @PostMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("При попытке изменить книгу с id: {} были введены некорректные данные", id);
            return "books_package/book_edit";
        }
        bookService.editBook(book, id);
        logger.info("Книга с id: {} была успешно редактирована", id);
        return "redirect:/books/{id}";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        logger.info("Книга с id: {} была удалена", id);
        return "redirect:/books";
    }
}
