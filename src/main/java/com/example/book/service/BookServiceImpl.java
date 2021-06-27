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
    public Book createBook(Book newBook) {
        return repository.save(newBook);
    }

    @Override
    public Book updateBook(Book newBook, Long id) {
        // FIXME: can this only be the line 47..49 code?
        return repository.findById(id).map(book -> {
            book.setName(newBook.getName());
            book.setAuthor(newBook.getAuthor());
            book.setIsbn(newBook.getIsbn());
            return repository.save(book);
        }).orElseGet(() -> {
            newBook.setId(id);

            return repository.save(newBook);
        });
    }

    @Override
    public void deleteBook(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Book> getBooksByAuthorId(Long id) {
        return repository.findByAuthorId(id);
    }
}
