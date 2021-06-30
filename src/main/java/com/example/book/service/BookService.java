package com.example.book.service;

import com.example.book.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getBooks();
    Book getBookById(Long id);
    void deleteBook(Long id);
}