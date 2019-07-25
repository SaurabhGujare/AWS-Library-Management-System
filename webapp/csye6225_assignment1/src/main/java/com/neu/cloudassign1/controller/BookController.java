package com.neu.cloudassign1.controller;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.exception.ImageExistsException;
import com.neu.cloudassign1.exception.ImageNotFoundException;
import com.neu.cloudassign1.exception.InvalidFileException;
import com.neu.cloudassign1.model.Book;
import com.neu.cloudassign1.model.CoverImage;
import com.neu.cloudassign1.service.BaseClient;
import com.neu.cloudassign1.service.BookService;
import com.neu.cloudassign1.service.ImageService;
import com.neu.cloudassign1.utils.Utility;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class BookController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BookService bookService;
    private ImageService imageService;
    private BaseClient baseClient;

    @Autowired
    private StatsDClient statsDClient;

    @Autowired
    public BookController(BookService bookService, ImageService imageService, BaseClient baseClient) {
        this.bookService = bookService;
        this.imageService = imageService;
        this.baseClient =baseClient;
    }


    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity createBook(@RequestBody Book book) {

        statsDClient.incrementCounter("endpoint.book.http.post");
        logger.info("Create Book: Start");

        Map<String,String> bookMap = new HashMap<>();
        book.setId(null);
        try{
            bookService.save(book);
            bookMap.clear();
            bookMap.put("successMessage","Book registered successfully");
            logger.info("Create Book: Success");
            return new ResponseEntity(bookMap, HttpStatus.OK);

        }catch(Exception e ){
            logger.error("Create Book: Failed");
            System.out.println("\n\nUnable to save the book"+e.getMessage());
            bookMap.clear();
            bookMap.put("errorMessage","Book is already registered");
            return new ResponseEntity(bookMap, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public List<Book> findAll() {

        statsDClient.incrementCounter("endpoint.book.http.get");
        logger.info("Find Books: Start");
        for(Book book: bookService.findAll()){
            //Set PresignedURL to CoverImage while returning all the books
            if(book.getCoverImage()!=null) {
                book.getCoverImage().setUri(
                        baseClient.GeneratePresignedURL(Utility.generateS3BucketObjectKey(
                                book.getCoverImage().getUri())).toString());
            }
        }
        logger.info("Find Books: Success");
        logger.info("Find Books: Stop");
        return bookService.findAll();

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public ResponseEntity getBook(@PathVariable UUID id) throws BookException {
        statsDClient.incrementCounter("endpoint.book.http.get");
        logger.info("Find Book: Start");

        try {
            //Set PresignedURL to CoverImage while returning a book
            if (bookService.findById(id).getCoverImage() != null){
                bookService.findById(id).getCoverImage().setUri(
                        baseClient.GeneratePresignedURL(Utility.generateS3BucketObjectKey(
                                bookService.findById(id).getCoverImage().getUri())).toString());
            }
            logger.info("Find Book: Success");
            logger.info("Find Book: Stop");
            return new ResponseEntity(bookService.findById(id), HttpStatus.OK);
        }
        catch (BookException e)
        {
            logger.error("Find Book: Failure");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }

    }

    @RequestMapping(value = "/book", method = RequestMethod.PUT)
    public ResponseEntity updateBook(@RequestBody Book book){

        statsDClient.incrementCounter("endpoint.book.http.put");
        logger.info("Update Book: Start");
        Map<String,String> bookMap = new HashMap<>();
        try {
            bookService.updateBook(book);
            bookMap.clear();
            bookMap.put("SuccessMessage","Book Updated Sucessfully");
            logger.info("Update Book: Success");
            logger.info("Update Book: Stop");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }catch(InvalidFormatException ife){
            bookMap.clear();
            bookMap.put("errorMessage","Unable to update");
            logger.error("Update Book: Failure: InvalidFormatException");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }catch(HttpMessageNotReadableException he){
            bookMap.clear();
            bookMap.put("errorMessage","Unable to update");
            logger.error("Update Book: Failure: HttpMessageNotReadableException");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            System.out.println(e.getMessage());
            bookMap.clear();
            bookMap.put("errorMessage","Unable to update");
            logger.error("Update Book: Failure");
            return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBook(@PathVariable UUID id){

        statsDClient.incrementCounter("endpoint.book.http.delete");
        logger.info("Delete Book: Start");
        Map<String,String> bookMap = new HashMap<>();
        try{
            Book book = bookService.getBookById(id);
            if(book==null){
                bookMap.put("errorMessage","Book with id "+id+" not found");
                logger.error("Delete Book: Failure: Book with id "+id+" not found");
                return new ResponseEntity(bookMap,HttpStatus.BAD_REQUEST);
            }else{
                bookService.deleteById(id);
                bookMap.put("successMessage", "Book with id " + id + " deleted successfully");
                logger.info("Delete Book: Success");
                logger.info("Delete Book: Stop");
                return new ResponseEntity(bookMap, HttpStatus.OK);
            }
        }catch (BookException be){
            bookMap.clear();
            bookMap.put("errorMessage", "Unable to delete book with id " + id );
            logger.error("Delete Book: Failure: Unable to delete book with id "+id);
            return new ResponseEntity(bookMap, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/book/{bookId}/image")
    public CoverImage addBookImage(@Valid @PathVariable UUID bookId, @RequestParam("file") MultipartFile file)
    {
        statsDClient.incrementCounter("endpoint.book.image.http.post");
        logger.info("Add Book: Start");


        CoverImage image;
        try
        {
            image = imageService.addBookImage(bookId, file);
        }
        catch (BookException e)
        {
            logger.error("Add Image: Failure: Book not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }
        catch (ImageExistsException e)
        {
            logger.error("Add Image: Failure: ImageExistsException");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
        catch (InvalidFileException e)
        {
            logger.error("Add Image: Failure: InvalidFileException");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }

        logger.info("Add Image: Success");
        logger.info("Add Image: Stop");
        return image;
    }

    @GetMapping("/book/{bookId}/image/{imageId}")
    public ResponseEntity getBookImage(@Valid @PathVariable UUID bookId, @PathVariable UUID imageId)
    {
        statsDClient.incrementCounter("endpoint.book.image.http.get");
        logger.info("Get Image: Start");

        Map<String, String> imageMap = new HashMap<String, String>();
        CoverImage image=null;
        Book book= null;
        try{
            book = bookService.getBookById(bookId);
            image = book.getCoverImage();
            if(book == null) {
                System.out.println("\n\nBook ids do not match");
                throw new BookException("No book is found with that id");
            }else if(!image.getId().equals(imageId)){
                throw new ImageNotFoundException("No image with the given Id found");
            }
            imageMap.put("id",image.getId().toString());
            //imageMap.put("url",image.getUri());

            //Set PresignedURL to CoverImage
            imageMap.put("url",baseClient.GeneratePresignedURL(
                    Utility.generateS3BucketObjectKey(image.getUri())).toString());

            logger.info("Get Image: Success");
            logger.info("Get Image: Stop");
            return new ResponseEntity(imageMap, HttpStatus.OK);
        }catch(BookException be){
            imageMap.put("error",be.getLocalizedMessage());
            logger.error("Get Image: Failure: Book not found");
            return new ResponseEntity(imageMap, HttpStatus.UNAUTHORIZED);
        }catch(ImageNotFoundException ie) {
            imageMap.put("error",ie.getLocalizedMessage());
            logger.error("Get Image: Failure: ImageNotFoundException");
            return new ResponseEntity(imageMap, HttpStatus.UNAUTHORIZED);
        }

    }

    @PutMapping("/book/{bookId}/image/{imageId}")
    public CoverImage updateBookImage(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID imageId, @RequestParam("file") MultipartFile file)
    {
        statsDClient.incrementCounter("endpoint.book.image.http.put");
        logger.info("Update Image: Start");

        CoverImage image;
        try
        {
            if(!bookService.getBookById(bookId).getCoverImage().getId().equals(imageId)){
                throw new ImageNotFoundException("Image with given id did not found");
            }
            image = imageService.updateBookImage(bookId, file);
        }
        catch(BookException e)
        {
            logger.error("Update Image: Failure: Book Not Found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }
        catch(InvalidFileException e)
        {
            logger.error("Update Image: Failure: InvalidFileException");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
        catch(ImageNotFoundException e){

            logger.error("Update Image: Failure: ImageNotFoundException");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }

        logger.info("Update Image: Success");
        logger.info("Update Image: Stop");
        return image;
    }

    @DeleteMapping("/book/{bookId}/image/{imageId}")
    public void deleteBookImage(@Valid @PathVariable UUID bookId, @Valid @PathVariable UUID imageId)
    {
        statsDClient.incrementCounter("endpoint.book.image.http.delete");
        logger.info("Delete Image: Start");
        try
        {
            if(!bookService.getBookById(bookId).getCoverImage().getId().equals(imageId)){
                throw new ImageNotFoundException("Image with given id did not found");
            }
            imageService.deleteBookImage(bookId);
            logger.info("Delete Image: Success");
            logger.info("Delete Image: Stop");
        }
        catch (BookException e)
        {
            logger.error("Delete Image: Failure: Book Not Found");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage(),e);
        } catch (ImageNotFoundException e) {
            logger.error("Delete Image: Failure: ImageNotFoundException");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage(),e);
        } catch (InvalidFileException e) {
            logger.error("Delete Image: Failure: InvalidFileException");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }

    }

}
