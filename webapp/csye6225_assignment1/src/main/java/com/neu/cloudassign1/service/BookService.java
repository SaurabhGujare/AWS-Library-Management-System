package com.neu.cloudassign1.service;


import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface BookService {



    public List<Book> findAll();

    public Book findById(UUID uuid) throws BookException;

    public void save(Book book);

    public void deleteById(UUID uuid) throws BookException;

    public Book getBookById(UUID id) throws BookException;

    public void updateBook(Book bookToUpdate) throws Exception;


}
