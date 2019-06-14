package com.neu.cloudassign1.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.neu.cloudassign1.dao.BookDAO;
import com.neu.cloudassign1.dao.BookRepository;
import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImplementation implements BookService {

    private BookRepository bookRepository;
    private BookDAO bookDAO;
    private EntityManager entityManager;

    @Autowired
    private BookServiceImplementation(BookRepository bookRepository, EntityManager entityManager, BookDAO bookDAO){
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
        this.bookDAO  =bookDAO;

    }

    @Override
    public List<Book> findAll() {
        List<Book> books = bookDAO.showBooks();
        return books;
        
    }

    @Override
    public Book findById(UUID uuid) throws BookException {
        Optional<Book> result = bookRepository.findById(uuid);
        Book book = null;
        if(result.isPresent()){
            book = result.get();
        }else{
            throw new BookException("Did not find employee with Id "+ uuid);
        }
        return book;
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteById(UUID uuid) {
        bookRepository.deleteById(uuid);
    }

    @Override
    public Book getBookById(UUID id) {
        return bookDAO.getBookById(id.toString());
    }

    @Override
    public void updateBook(Book book) throws Exception {
        try {
            Book bookToUpdate = bookDAO.getBookById(book.getId().toString());
            bookDAO.updateBook(bookToUpdate, book);
        }catch(InvalidFormatException ife){
            throw new InvalidFormatException(ife.getMessage(),ife.getPathReference(),ife.getTargetType());
        }catch(HttpMessageNotReadableException he){
            throw new HttpMessageNotReadableException(he.getMessage());
        }catch(Exception e){
            throw new Exception();
        }
    }

    @Override
    public void deleteBookByTitle(Book book) throws BookException {
        Book bookToDelete = bookDAO.getBookById(book.getId().toString());
        bookDAO.deleteBookByTitle(bookToDelete.getTitle());
    }

/*
    @Override
    public void saveBook(Book b) {
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.saveOrUpdate(b);
    }

    @Override
    public boolean isBook(String title) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("FROM Book WHERE title=:unm ");
        theQuery.setParameter("unm", title);

        List<Book> list = theQuery.getResultList();

        if(list.isEmpty())
            return false;

        return true ;
    }

    @Override
    public List<Book> showBooks() {
//        Session currentSession = entityManager.unwrap(Session.class);
//
//        Query theQuery = currentSession.createQuery("FROM Book");
//
//        List<Book> list = theQuery.getResultList();

        return bookRepository.findAll();
    }



    @Override
    public void deleteBookByTitle(String title) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("delete From Book WHERE title=:unm ");

        theQuery.setParameter("unm", title);

        theQuery.executeUpdate();

    }

    @Override
    public void updateBook(Book bookToUpdate, Book book) throws BookException {

        try {
//            System.out.println("Inside UpdateDao");
//            System.out.println("BookToUpdate "+bookToUpdate);
//            System.out.println("BookData "+book);
//            Session currentSession = entityManager.unwrap(Session.class);
//            System.out.println("After session");
//            Query theQuery = currentSession.createQuery("UPDATE Book set " +
//                    "title=:title, author=:author, isbn=:isbn, quantity =:quantity WHERE title =:titleCrit");
//            System.out.println("After query");
//            theQuery.setParameter("author", book.getAuthor());
//            theQuery.setParameter("isbn", book.getIsbn());
//            theQuery.setParameter("title", book.getTitle());
//            theQuery.setParameter("quantity", book.getQuantity());
//            theQuery.setParameter("titleCrit",bookToUpdate.getTitle());
//            System.out.println("before update");
//            System.out.println("\n\n\nRows Updated "+theQuery.executeUpdate());
            bookRepository.save(book);

        }catch(Exception e){
            throw new BookException("Unable to update the book "+e.getMessage());
        }

    }*/

}
