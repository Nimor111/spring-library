package com.example.book;

import com.example.book.dto.BookDTO;
import com.example.book.model.Author;
import com.example.book.model.Book;
import com.example.book.repository.AuthorRepository;
import com.example.book.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class BookApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private AuthorRepository authorRepo;

    @BeforeEach
    public void setUp() {
        bookRepo.deleteAll();

        Author author = new Author("Ivan", "bulgarian");
        author.setId(1L);
        authorRepo.save(author);
    }

    @Test
    public void shouldReturnNoBooksAtStart() throws Exception {
        mockMvc.perform(get("/api/v1/books"))
            .andExpect(status().isOk())
            .andExpect(content().json(om.writeValueAsString(Collections.emptyList())));
    }

    @Test
    public void shouldCreateANewBookSuccessfully() throws Exception {
        BookDTO newBook = new BookDTO("Book1", 1L, "978-3-16-148410-0");
        String bookJson = om.writeValueAsString(newBook);

        Author author = authorRepo.getById(1L);
        Book resultBook = new Book(newBook.getName(), author, newBook.getIsbn());
        resultBook.setId(1L);

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newBook.getName()))
                .andExpect(jsonPath("$.isbn").value(newBook.getIsbn()));
    }

    @Test
    public void shouldFailOnEmptyFieldWhenCreatingABook() throws Exception {
        BookDTO newBook = new BookDTO(null, 1L, "978-3-16-148410-0");
        String bookJson = om.writeValueAsString(newBook);

        Map<String, String> errors = new HashMap<>();
        errors.put("name", "Name is mandatory!");

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(om.writeValueAsString(errors)));
    }

    @Test
    public void shouldFailOnTooShortNameWhenCreatingABook() throws Exception {
        BookDTO newBook = new BookDTO("na", 1L, "978-3-16-148410-0");
        String bookJson = om.writeValueAsString(newBook);

        Map<String, String> errors = new HashMap<>();
        errors.put("name", "Name must be between 3 and 255 symbols");

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(om.writeValueAsString(errors)));
    }

    @Test
    public void shouldUpdateBookSuccessfully() throws Exception {
        Author author = authorRepo.getById(1L);
        Book existingBook = new Book("book1", author, "978-3-16-148410-0");
        Book saved = bookRepo.save(existingBook);

        BookDTO updatedBook = new BookDTO("newBook", 1L, "978-3-16-148410-0");
        String bookJson = om.writeValueAsString(updatedBook);

        Author newAuthor = authorRepo.getById(updatedBook.getAuthorId());

        Book resultBook = new Book(updatedBook.getName(), newAuthor, updatedBook.getIsbn());
        resultBook.setId(saved.getId());

        mockMvc.perform(put(String.format("/api/v1/books/%d", saved.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(resultBook)));

        // Make sure it's updated in the db
        mockMvc.perform(get(String.format("/api/v1/books/%d", saved.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(resultBook)));
    }

    @Test
    public void shouldDeleteBookSuccessfully() throws Exception {
        Author author = authorRepo.getById(1L);
        Book existingBook = new Book("book1", author, "978-3-16-148410-0");
        Book savedBook = bookRepo.save(existingBook);

        mockMvc.perform(delete(String.format("/api/v1/books/%d", savedBook.getId())))
                .andExpect(status().isOk());

        // Make sure it doesn't exist anymore
        mockMvc.perform(get("/api/v1/books/"))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(Collections.emptyList())));
    }
}
