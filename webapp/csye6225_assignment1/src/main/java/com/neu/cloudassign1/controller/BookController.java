package com.neu.cloudassign1.controller;


import com.neu.cloudassign1.dao.BookDAO;
import com.neu.cloudassign1.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    private BookDAO bookDAO;

    @Autowired
    public BookController(BookDAO bDAO) {
        this.bookDAO = bDAO;
    }


    @RequestMapping(value = "/book", method = RequestMethod.POST)
    @ResponseBody
    public String createBook(@RequestBody Book book) {

        book.setId(null);

        if (bookDAO.isBook(book.getTitle())) {
            return "There is already a book saved with the title provided";
        }

        bookDAO.saveBook(book);
        return book.getId().toString();
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    @ResponseBody
    public List<Book> showBooks() {

        List<Book> books = bookDAO.showBooks();

        return books;

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Book showBook(@PathVariable String id) {

        Book book = bookDAO.getBookById(id);

        if(book == null) {
            throw new RuntimeException("Book id not found :" +id);
        }

        return book;

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateBook(@RequestBody Book book, @PathVariable String id) {

        Book bb= bookDAO.getBookById(id);

        if(!book.getTitle().isEmpty())
            bb.setTitle(book.getTitle());

        if(!book.getAuthor().isEmpty())
            bb.setAuthor(book.getAuthor());

        bookDAO.saveBook(bb);

        return "Book updated!!!!!!";

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteBook(@PathVariable String id) {

        Book book = bookDAO.getBookById(id);

        if(book == null) {
            throw new RuntimeException("Book id not found :" +id);
        }

        bookDAO.deleteBookByTitle(book.getTitle());

        return "Book deleted!!!!!!" +id;

    }


}
