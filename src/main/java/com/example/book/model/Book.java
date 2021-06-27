package com.example.book.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Null;

@Entity
@Proxy(lazy = false)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name="author_id")
    private Author author;

    @Column
    private String isbn;

    @Column
    private Long storeId = 1L;

    public Book(String name, Author author, String isbn) {
        this.name = name;
        this.author = author;
        this.isbn = isbn;
    }

    public Book(String name, Author author, String isbn, Long storeId) {
        this.name = name;
        this.author = author;
        this.isbn = isbn;
        this.storeId = storeId;
    }

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

}
