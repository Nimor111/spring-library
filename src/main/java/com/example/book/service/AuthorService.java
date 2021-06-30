package com.example.book.service;

import com.example.book.model.Author;
import com.example.book.model.Book;

import java.util.List;

public interface AuthorService {
    List<Author> getAuthors();
    Author getAuthorById(Long id);
    Author createAuthor(Author author);
    Author updateAuthor(Author author, Long id);
    void deleteAuthor(Long id);
    List<Book> getBooksByAuthorId(Long id);
    Book addBookToAuthor(Long id, Book book);
    Book replaceAuthorBook(Long id, Long bookId, Book updatedBook);
}
