package com.example.book.service;

import com.example.book.repository.BookRepository;
import com.example.book.exception.BookNotFoundException;
import com.example.book.model.Book;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Book> getBooks() {
        return repository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public void deleteBook(Long id) {
        repository.deleteById(id);
    }

}
