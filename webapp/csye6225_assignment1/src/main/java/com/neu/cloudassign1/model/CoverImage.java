package com.neu.cloudassign1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="coverImage")
public class CoverImage {

    @Id
    @Column(length = 16)
    private UUID id;

    private String uri;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Book book;

    public CoverImage() {
    }

    public CoverImage(UUID id, String uri, Book book) {
        this.id = id;
        this.uri = uri;
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
