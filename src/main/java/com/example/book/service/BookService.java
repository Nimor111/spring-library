package com.example.book.service;

import com.example.book.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getBooks();
    Book getBookById(Long id);
    Book createBook(Book book);
    Book updateBook(Book book, Long id);
    void deleteBook(Long id);
    List<Book> getBooksByAuthorId(Long id);
}