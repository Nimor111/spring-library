package com.example.book.service;

import com.example.book.exception.AuthorNotFoundException;
import com.example.book.exception.BookNotFoundException;
import com.example.book.model.Author;
import com.example.book.model.Book;
import com.example.book.repository.AuthorRepository;
import com.example.book.repository.BookRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Author> getAuthors() {
        return repository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public Author createAuthor(Author newAuthor) {
        return repository.save(newAuthor);
    }

    @Override
    public Author updateAuthor(Author newAuthor, Long id) {
        return repository.findById(id).map(author -> {
            author.setName(newAuthor.getName());
            author.setNationality(newAuthor.getNationality());
            return repository.save(author);
        }).orElseGet(() -> {
            newAuthor.setId(id);

            return repository.save(newAuthor);
        });
    }

    @Override
    public void deleteAuthor(Long id) {
        repository.deleteById(id);
    }
}
