package com.example.book;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/v1/api/books")
    List<Book> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/v1/api/books/{bookId}")
    Book getBook(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @PostMapping("/v1/api/books")
    Book createBook(@RequestBody Book newBook) {
        return bookService.createBook(newBook);
    }

    @PutMapping("/v1/api/books/{bookId}")
    Book replaceBook(@RequestBody Book newBook, @PathVariable Long bookId) {
        return bookService.updateBook(newBook, bookId);
    }

    @DeleteMapping("/v1/api/books/{bookId}")
    void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    }
}
