package com.example.book;

import com.example.book.client.StoreClient;
import com.example.book.client.StoreDTO;
import com.example.book.client.StoreType;
import com.example.book.dto.BookDTO;
import com.example.book.model.Author;
import com.example.book.model.Book;
import com.example.book.repository.AuthorRepository;
import com.example.book.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    // FIXME not a big fan of this, but I guess the point is to test the controller and the business logic of the services
    @MockBean
    private BookRepository bookRepo;

    @MockBean
    private AuthorRepository authorRepo;

    @MockBean
    private StoreClient storeClient;

    @BeforeEach
    public void setUp() {
        Author author = new Author("Ivan", "bulgarian");
        author.setId(1L);
        Mockito.when(authorRepo.getById(1L)).thenReturn(author);
        Mockito.when(authorRepo.findById(1L)).thenReturn(Optional.of(author));

        Mockito.when(storeClient.getStoreById(1L)).thenReturn(new StoreDTO("store1", StoreType.Large));
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

        Mockito.when(bookRepo.save(Mockito.any(Book.class))).thenReturn(resultBook);

        mockMvc.perform(post(String.format("/api/v1/authors/%d/books", 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(resultBook.getName()))
                .andExpect(jsonPath("$.isbn").value(resultBook.getIsbn()));
    }

    @Test
    public void shouldFailOnEmptyFieldWhenCreatingABook() throws Exception {
        BookDTO newBook = new BookDTO(null, 1L, "978-3-16-148410-0");
        String bookJson = om.writeValueAsString(newBook);

        Map<String, String> errors = new HashMap<>();
        errors.put("name", "Name is mandatory!");

        mockMvc.perform(post(String.format("/api/v1/authors/%d/books", 1L))
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

        mockMvc.perform(post(String.format("/api/v1/authors/%d/books", 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(om.writeValueAsString(errors)));
    }

    @Test
    public void shouldUpdateBookSuccessfully() throws Exception {
        Author author = authorRepo.getById(1L);
        Book existingBook = new Book("book1", author, "978-3-16-148410-0");
        existingBook.setId(1L);

        Mockito.when(bookRepo.findById(existingBook.getId())).thenReturn(Optional.of(existingBook));

        BookDTO updatedBook = new BookDTO("newBook", 1L, "978-3-16-148410-0");
        String bookJson = om.writeValueAsString(updatedBook);

        Book resultBook = new Book(updatedBook.getName(), author, updatedBook.getIsbn());
        resultBook.setId(existingBook.getId());

        Mockito.when(bookRepo.save(Mockito.any(Book.class))).thenReturn(resultBook);

        mockMvc.perform(put(String.format("/api/v1/authors/%d/books/%d", author.getId(), existingBook.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(resultBook)));
    }

    @Test
    public void shouldDeleteBookSuccessfully() throws Exception {
        Author author = authorRepo.getById(1L);
        Book existingBook = new Book("book1", author, "978-3-16-148410-0");
        existingBook.setId(1L);

        Mockito.when(bookRepo.findById(existingBook.getId())).thenReturn(Optional.of(existingBook));

        Mockito.doNothing().when(bookRepo).deleteById(existingBook.getId());

        mockMvc.perform(delete(String.format("/api/v1/books/%d", existingBook.getId())))
                .andExpect(status().isOk());
    }
}
