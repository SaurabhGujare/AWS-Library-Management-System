package com.neu.cloudassign1.service;


import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.model.Book;

import java.util.List;
import java.util.UUID;

public interface BookService {

//    public void saveBook(Book b);
//
//    public boolean isBook(String title);
//
//    public List<Book> showBooks();
//
//    public Book getBookById(String id);
//
//    public void deleteBookByTitle(String title);
//
//    public void updateBook(Book bookToUpdate, Book book) throws BookException;

    public List<Book> findAll();

    public Book findById(UUID uuid) throws BookException;

    public void save(Book book);

    public void deleteById(UUID uuid);

    public Book getBookById(UUID id);

    public void updateBook(Book bookToUpdate) throws Exception;

    public void deleteBookByTitle(Book book) throws Exception;
}
