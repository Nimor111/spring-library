package com.example.book;

import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {
    List<Book> getBooks();
    Book getBookById(Long id);
    Book createBook(Book book);
    Book updateBook(Book book, Long id);
    void deleteBook(Long id);
}