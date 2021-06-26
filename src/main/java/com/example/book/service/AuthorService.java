package com.example.book.service;

import com.example.book.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAuthors();
    Author getAuthorById(Long id);
    Author createAuthor(Author author);
    Author updateAuthor(Author book, Long id);
    void deleteAuthor(Long id);
}
