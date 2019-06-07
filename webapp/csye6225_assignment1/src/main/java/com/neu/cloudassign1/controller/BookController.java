package com.neu.cloudassign1.controller;


import com.neu.cloudassign1.dao.BookDAO;
import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BookController {
    private BookDAO bookDAO;

    @Autowired
    public BookController(BookDAO bDAO) {
        this.bookDAO = bDAO;
    }


    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity createBook(@RequestBody Book book) {

        Map<String,String> bookMap = new HashMap<>();
        book.setId(null);

        if (bookDAO.isBook(book.getTitle())) {
            bookMap.clear();
            bookMap.put("errorMessage","Book is already registered");
            return new ResponseEntity(bookMap, HttpStatus.BAD_REQUEST);
        }

        bookDAO.saveBook(book);
        bookMap.clear();
        bookMap.put("successMessage","Book registered successfully");
        return new ResponseEntity(bookMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public List<Book> showBooks() {

        List<Book> books = bookDAO.showBooks();

        return books;

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public ResponseEntity showBook(@PathVariable String id) {
        Map<String,String> bookMap = new HashMap<>();
        Book book = bookDAO.getBookById(id);

         System.out.println(book);
        if(book.getTitle() == null) {
            bookMap.put("errorMessage","Book with id "+id+" not found");
            return new ResponseEntity(bookMap,HttpStatus.NOT_FOUND);
        }

        bookMap.clear();
        bookMap.put("id", book.getId().toString());
        bookMap.put("title",book.getTitle());
        bookMap.put("author",book.getAuthor());
        bookMap.put("isbn",book.getIsbn());
        bookMap.put("quantity",Integer.toString(book.getQuantity()));
        return new ResponseEntity(bookMap,HttpStatus.OK);

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateBook(@RequestBody Book book, @PathVariable String id) throws BookException {
        Map<String,String> bookMap = new HashMap<>();
        try{
            System.out.println("Inside Update Controller");
            Book bookToUpdate= bookDAO.getBookById(id);

            if(bookToUpdate.getId()==null) throw new Exception();

            System.out.println("BookToUpdate\n"+bookToUpdate);
            bookDAO.updateBook(bookToUpdate,book);
            bookMap.put("successMessage","Book with id "+id+" updated successfully");
            return new ResponseEntity(bookMap,HttpStatus.OK);
        }catch(Exception e){
            bookMap.clear();
            bookMap.put("errorMessage","Unable to update");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBook(@PathVariable String id) {
        Map<String,String> bookMap = new HashMap<>();
        Book book = bookDAO.getBookById(id);

        if(book.getTitle() == null) {
            bookMap.put("errorMessage","Book with id "+id+" not found");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }

        bookDAO.deleteBookByTitle(book.getTitle());
        bookMap.put("successMessage","Book with id "+id+" deleted successfully");
        return new ResponseEntity(bookMap,HttpStatus.OK);

    }


}
