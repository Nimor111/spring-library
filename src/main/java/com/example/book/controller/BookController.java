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

    // FIXME: Paging?
    @GetMapping("/api/v1/books")
    public List<Book> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/api/v1/books/{bookId}")
    public Book getBook(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @PostMapping("api/v1/books")
    public Book createBook(@Valid @RequestBody BookDTO newBook) {
        // FIXME: These should be in the service
        Author author = authorService.getAuthorById(newBook.getAuthorId());
        StoreDTO store = storeClient.getStoreById(newBook.getStoreId());
        var bookToCreate = new Book(newBook.getName(), author, newBook.getIsbn(), newBook.getStoreId());
        return bookService.createBook(bookToCreate);
    }

    @PutMapping("/api/v1/books/{bookId}")
    public Book replaceBook(@Valid @RequestBody BookDTO newBook, @PathVariable Long bookId) {
        // FIXME: These should be in the service
        Author author = authorService.getAuthorById(newBook.getAuthorId());
        StoreDTO store = storeClient.getStoreById(newBook.getStoreId());
        var bookToUpdate = new Book(newBook.getName(), author, newBook.getIsbn(), newBook.getStoreId());
        return bookService.updateBook(bookToUpdate, bookId);
    }

    @DeleteMapping("/api/v1/books/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    }

    @GetMapping("/api/v1/authors/{authorId}/books")
    public List<Book> getBooksByAuthorId(@PathVariable Long authorId) {
        return bookService.getBooksByAuthorId(authorId);
    }

    // FIXME: REST Actions - never use verbs
    // books/{bookId}/read
    // books/{bookId}/status
}
