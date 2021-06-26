package com.example.book;

import com.example.book.dto.BookDTO;
import com.example.book.model.Book;
import com.example.book.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
    private BookRepository repo;

    @BeforeEach
    public void cleanUp() {
        repo.deleteAll();
    }

    @Test
    public void shouldReturnNoBooksAtStart() throws Exception {
        mockMvc.perform(get("/api/v1/books"))
            .andExpect(status().isOk())
            .andExpect(content().json(om.writeValueAsString(Collections.emptyList())));
    }

    @Test
    public void shouldCreateANewBookSuccessfully() throws Exception {
        BookDTO newBook = new BookDTO("Book1", "Author1", "978-3-16-148410-0");
        String bookJson = om.writeValueAsString(newBook);

        Book resultBook = new Book(newBook.getName(), newBook.getAuthor(), newBook.getIsbn());
        resultBook.setId(1L);

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(resultBook)));
    }

    @Test
    public void shouldFailOnEmptyFieldWhenCreatingABook() throws Exception {
        BookDTO newBook = new BookDTO(null, "Author1", "978-3-16-148410-0");
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
        BookDTO newBook = new BookDTO("na", "Author1", "978-3-16-148410-0");
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
        Book existingBook = new Book("book1", "author1", "978-3-16-148410-0");
        Book saved = repo.save(existingBook);

        BookDTO updatedBook = new BookDTO("newBook", "newAuthor", "978-3-16-148410-0");
        String bookJson = om.writeValueAsString(updatedBook);

        Book resultBook = new Book(updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getIsbn());
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
        Book existingBook = new Book("book1", "author1", "978-3-16-148410-0");
        Book savedBook = repo.save(existingBook);

        mockMvc.perform(delete(String.format("/api/v1/books/%d", savedBook.getId())))
                .andExpect(status().isOk());

        // Make sure it doesn't exist anymore
        mockMvc.perform(get("/api/v1/books/"))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(Collections.emptyList())));
    }
}
