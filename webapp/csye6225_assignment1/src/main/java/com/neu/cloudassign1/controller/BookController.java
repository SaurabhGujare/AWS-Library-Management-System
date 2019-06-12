package com.neu.cloudassign1.controller;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.neu.cloudassign1.dao.BookDAO;
import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.model.Book;
import com.neu.cloudassign1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BookController {
//    private BookDAO bookDAO;
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity createBook(@RequestBody Book book) {

        Map<String,String> bookMap = new HashMap<>();
        book.setId(null);
        try{
            bookService.save(book);
            bookMap.clear();
            bookMap.put("successMessage","Book registered successfully");
            return new ResponseEntity(bookMap, HttpStatus.OK);

        }catch(Exception e ){
            System.out.println("\n\nUnable to save the book"+e.getMessage());
            bookMap.clear();
            bookMap.put("errorMessage","Book is already registered");
            return new ResponseEntity(bookMap, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public List<Book> findAll() {

        return bookService.findAll();

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public ResponseEntity getBook(@PathVariable UUID id) throws BookException {

        Map<String,String> bookMap = new HashMap<>();
        try{
            Book book = bookService.getBookById(id);
            if(book == null){
                bookMap.put("errorMessage","Book with id "+id+" not found");
                return new ResponseEntity(bookMap, HttpStatus.NOT_FOUND);
            }else{
                bookMap.put("id", book.getId().toString());
                bookMap.put("title",book.getTitle());
                bookMap.put("author",book.getAuthor());
                bookMap.put("isbn",book.getIsbn());
                bookMap.put("quantity",Integer.toString(book.getQuantity()));
                return new ResponseEntity(bookMap, HttpStatus.OK);
            }

        } catch(Exception e){
            bookMap.clear();
            bookMap.put("errorMessage","Unable to get the book");
            return new ResponseEntity(bookMap, HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/book", method = RequestMethod.PUT)
    public ResponseEntity updateBook(@RequestBody Book book){

        Map<String,String> bookMap = new HashMap<>();
        try {
            bookService.updateBook(book);
            bookMap.clear();
            bookMap.put("SuccessMessage","Book Updated Sucessfully");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }catch(InvalidFormatException ife){
            bookMap.clear();
            bookMap.put("errorMessage","Unable to update");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }catch(HttpMessageNotReadableException he){
            bookMap.clear();
            bookMap.put("errorMessage","Unable to update");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            System.out.println(e.getMessage());
            bookMap.clear();
            bookMap.put("errorMessage","Unable to update");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBook(@PathVariable UUID id) throws BookException {
        Map<String,String> bookMap = new HashMap<>();
        try{
            Book book = bookService.getBookById(id);
            if(book==null){
                bookMap.put("errorMessage","Book with id "+id+" not found");
                return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
            }else {
                bookService.deleteBookByTitle(book);
                bookMap.put("successMessage", "Book with id " + id + " deleted successfully");
                return new ResponseEntity(bookMap, HttpStatus.OK);
            }
        }catch (BookException be){
            bookMap.clear();
            bookMap.put("errorMessage", "Unable to delete book with id " + id );
            return new ResponseEntity(bookMap, HttpStatus.OK);

        } catch (Exception e) {
            bookMap.clear();
            bookMap.put("errorMessage", "Unable to delete book with id " + id );
            return new ResponseEntity(bookMap, HttpStatus.OK);
        }

    }

}
