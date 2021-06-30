package com.example.book.controller;

import com.example.book.dto.AuthorDTO;
import com.example.book.dto.BookDTO;
import com.example.book.model.Author;
import com.example.book.model.Book;
import com.example.book.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/api/v1/authors")
    List<Author> getAuthors() {
        return authorService.getAuthors();
    }

    @GetMapping("/api/v1/authors/{authorId}")
    Author getAuthor(@PathVariable Long authorId) {
        return authorService.getAuthorById(authorId);
    }

    @PostMapping("/api/v1/authors")
    Author createAuthor(@Valid @RequestBody AuthorDTO newAuthor) {
        var authorToCreate = new Author(newAuthor.getName(), newAuthor.getNationality());
        return authorService.createAuthor(authorToCreate);
    }

    @PostMapping("/api/v1/authors/{authorId}/books")
    Book createBook(@Valid @RequestBody BookDTO newBook, @PathVariable Long authorId) {
        // FIXME: These should be in the service
        Author author = authorService.getAuthorById(authorId);
        var bookToCreate = new Book(newBook.getName(), author, newBook.getIsbn(), newBook.getStoreId());
        return authorService.addBookToAuthor(authorId, bookToCreate);
    }

    @PutMapping("/api/v1/authors/{authorId}/books/{bookId}")
    Book replaceBook(@Valid @RequestBody BookDTO newBook, @PathVariable Long authorId, @PathVariable Long bookId) {
        Author author = authorService.getAuthorById(authorId);
        var bookToReplace = new Book(newBook.getName(), author, newBook.getIsbn(), newBook.getStoreId());
        return authorService.replaceAuthorBook(authorId, bookId, bookToReplace);
    }

    @PutMapping("/api/v1/authors/{authorId}")
    Author replaceAuthor(@Valid @RequestBody AuthorDTO newAuthor, @PathVariable Long authorId) {
        var authorToUpdate = new Author(newAuthor.getName(), newAuthor.getNationality());
        return authorService.updateAuthor(authorToUpdate, authorId);
    }

    @GetMapping("/api/v1/authors/{authorId}/books")
    public List<Book> getBooksByAuthorId(@PathVariable Long authorId) {
        return authorService.getBooksByAuthorId(authorId);
    }

    @DeleteMapping("/api/v1/authors/{authorId}")
    void deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthor(authorId);
    }
}
