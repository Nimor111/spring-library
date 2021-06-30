package com.example.book.controller;

import com.example.book.model.Author;
import com.example.book.service.AuthorService;
import com.example.book.service.BookService;
import com.example.book.dto.BookDTO;
import com.example.book.model.Book;
import com.example.book.client.StoreClient;
import com.example.book.client.StoreDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final StoreClient storeClient;

    public BookController(BookService bookService, AuthorService authorService, StoreClient storeClient) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.storeClient = storeClient;
    }

    @GetMapping("/api/v1/books")
    public List<Book> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/api/v1/books/{bookId}")
    public Book getBook(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @DeleteMapping("/api/v1/books/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    }

    // FIXME: REST Actions - never use verbs
    // books/{bookId}/read
    // books/{bookId}/status
}
