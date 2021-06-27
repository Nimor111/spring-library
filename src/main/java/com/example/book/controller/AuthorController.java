package com.example.book.controller;

import com.example.book.dto.AuthorDTO;
import com.example.book.model.Author;
import com.example.book.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("api/v1/authors")
    Author createAuthor(@Valid @RequestBody AuthorDTO newAuthor) {
        var authorToCreate = new Author(newAuthor.getName(), newAuthor.getNationality());
        return authorService.createAuthor(authorToCreate);
    }

    @PutMapping("/api/v1/authors/{authorId}")
    Author replaceAuthor(@Valid @RequestBody AuthorDTO newAuthor, @PathVariable Long authorId) {
        var authorToUpdate = new Author(newAuthor.getName(), newAuthor.getNationality());
        return authorService.updateAuthor(authorToUpdate, authorId);
    }

    @DeleteMapping("/api/v1/authors/{authorId}")
    void deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthor(authorId);
    }
}
