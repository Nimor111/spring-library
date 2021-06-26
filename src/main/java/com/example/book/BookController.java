package com.example.book;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
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
        var bookToCreate = new Book(newBook.getName(), newBook.getAuthor(), newBook.getIsbn());
        return bookService.createBook(bookToCreate);
    }

    @PutMapping("/api/v1/books/{bookId}")
    Book replaceBook(@Valid @RequestBody BookDTO newBook, @PathVariable Long bookId) {
        var bookToUpdate = new Book(newBook.getName(), newBook.getAuthor(), newBook.getIsbn());
        return bookService.updateBook(bookToUpdate, bookId);
    }

    @DeleteMapping("/api/v1/books/{bookId}")
    void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
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
