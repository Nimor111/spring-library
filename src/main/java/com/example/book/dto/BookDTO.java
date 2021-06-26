package com.example.book.dto;

import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BookDTO {

    @NotBlank(message = "Name is mandatory!")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 symbols")
    private String name;

    @NotBlank(message = "Author is mandatory!")
    private String author;

    @ISBN
    @NotBlank
    private String isbn;

    public BookDTO(String name, String author, String isbn) {
        this.name = name;
        this.author = author;
        this.isbn = isbn;
    }

    public BookDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
