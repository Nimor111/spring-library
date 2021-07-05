package com.example.book.service;

import com.example.book.client.StoreClient;
import com.example.book.client.StoreDTO;
import com.example.book.exception.AuthorNotFoundException;
import com.example.book.model.Author;
import com.example.book.model.Book;
import com.example.book.repository.AuthorRepository;
import com.example.book.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;
    private StoreClient storeClient;
    private BookRepository bookRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, StoreClient storeClient, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.storeClient = storeClient;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository
                .findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public Author createAuthor(Author newAuthor) {
        return authorRepository.save(newAuthor);
    }

    @Override
    public Author updateAuthor(Author newAuthor, Long id) {
        return authorRepository.findById(id).map(author -> {
            author.setName(newAuthor.getName());
            author.setNationality(newAuthor.getNationality());
            return authorRepository.save(author);
        }).orElseGet(() -> {
            newAuthor.setId(id);

            return authorRepository.save(newAuthor);
        });
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public Book addBookToAuthor(Long id, Book book) {
        StoreDTO store = storeClient.getStoreById(book.getStoreId());
        return bookRepository.save(book);
    }

    @Override
    public Book replaceAuthorBook(Long id, Long bookId, Book updatedBook) {
        StoreDTO store = storeClient.getStoreById(updatedBook.getStoreId());
        return bookRepository.findById(bookId).map(book -> {
            book.setAuthor(updatedBook.getAuthor());
            book.setName(updatedBook.getName());
            book.setIsbn(updatedBook.getIsbn());
            book.setStoreId(updatedBook.getStoreId());

            return bookRepository.save(book);
        }).orElseGet(() ->
            bookRepository.save(updatedBook)
        );
    }

    @Override
    public List<Book> getBooksByAuthorId(Long id) {
        return bookRepository.findByAuthorId(id);
    }
}
