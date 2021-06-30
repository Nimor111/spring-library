package com.example.book.dto;

import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BookDTO {

    @NotBlank(message = "Name is mandatory!")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 symbols")
    private String name;

    @ISBN
    @NotBlank
    private String isbn;

    // FIXME: This should not be the default
    @NotNull
    private Long storeId = 1L;

    public BookDTO(String name, String isbn) {
        this.name = name;
        this.isbn = isbn;
    }

    public BookDTO(String name, Long storeId, String isbn) {
        this.name = name;
        this.isbn = isbn;
        this.storeId = storeId;
    }

    public BookDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

}
