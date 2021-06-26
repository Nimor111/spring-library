package com.example.book.controller;

import com.example.book.model.Author;
import com.example.book.repository.AuthorRepository;
import com.example.book.service.AuthorService;
import com.example.book.service.BookService;
import com.example.book.dto.BookDTO;
import com.example.book.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/api/v1/books")
    List<Book> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/api/v1/books/{bookId}")
    Book getBook(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @PostMapping("api/v1/books")
    Book createBook(@Valid @RequestBody BookDTO newBook) {
        Author author = authorService.getAuthorById(newBook.getAuthorId());
        var bookToCreate = new Book(newBook.getName(), author, newBook.getIsbn());
        return bookService.createBook(bookToCreate);
    }

    @PutMapping("/api/v1/books/{bookId}")
    Book replaceBook(@Valid @RequestBody BookDTO newBook, @PathVariable Long bookId) {
        Author author = authorService.getAuthorById(newBook.getAuthorId());
        var bookToUpdate = new Book(newBook.getName(), author, newBook.getIsbn());
        return bookService.updateBook(bookToUpdate, bookId);
    }

    @DeleteMapping("/api/v1/books/{bookId}")
    void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    }

    @GetMapping("/api/v1/authors/{authorId}/books")
    List<Book> getBooksByAuthorId(@PathVariable Long authorId) {
        return bookService.getBooksByAuthorId(authorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }}
