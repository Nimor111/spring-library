package com.example.book.dto;

import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthorDTO {
    @NotBlank(message = "Name is mandatory!")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 symbols")
    private String name;

    private String nationality;

    public AuthorDTO() {
    }

    public AuthorDTO(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
