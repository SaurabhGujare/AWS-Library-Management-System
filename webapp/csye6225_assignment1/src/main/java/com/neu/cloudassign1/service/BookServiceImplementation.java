package com.neu.cloudassign1.service;

import com.neu.cloudassign1.repository.BookRepository;
import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImplementation implements BookService {

    private BookRepository bookRepository;


    @Autowired
    private BookServiceImplementation(BookRepository bookRepository){
        this.bookRepository = bookRepository;

    }

    @Override
    public List<Book> findAll() {

        return bookRepository.findAll();
    }

    @Override
    public Book findById(UUID uuid) throws BookException {
        Optional<Book> result = bookRepository.findById(uuid);
        Book book = null;
        if(result.isPresent()){
            book = result.get();
        }else{
            throw new BookException("Did not find book with Id "+ uuid);
        }
        return book;
    }

    @Override
    public void save(Book book) {

        bookRepository.save(book);
    }

    @Override
    public void deleteById(UUID id) throws BookException {

        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty())
        {
            throw new BookException("Could not find book with id : "+id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBookById(UUID id) throws BookException {

        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty())
        {
            throw new BookException("Could not find book with id : "+id);
        }
        return book.get();
    }

    @Override
    public void updateBook(Book book) throws BookException {
        Optional<Book> existingBook = bookRepository.findById(book.getId());
        if(existingBook.isEmpty())
        {
            throw new BookException("Could not find book with id : "+book.getId());
        }
        bookRepository.save(book);

    }



}
