package com.neu.cloudassign1.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private long id;

    @Column(name="title")
    @NotNull(message="is required")
    private String title;

    @Column(name="author")
    @NotNull(message="is required")
    private String author;

    @Column(name="isbn")
    @NotNull(message="is required")
    private String isbn;

    @Column(name="quantity")
    @NotNull(message="is required")
    private int quantity;

    public Book() {

    }

    public Book(long id, String title,  String author,  String isbn,  int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
