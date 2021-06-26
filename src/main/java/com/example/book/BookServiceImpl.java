package com.example.book;

import org.springframework.stereotype.Service;

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
}
