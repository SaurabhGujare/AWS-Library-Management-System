package com.neu.cloudassign1.service;

import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.exception.ImageExistsException;
import com.neu.cloudassign1.exception.InvalidFileException;
import com.neu.cloudassign1.model.Book;
import com.neu.cloudassign1.model.CoverImage;
import com.neu.cloudassign1.repository.BookRepository;
import com.neu.cloudassign1.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Profile("local")
@Service
public class ImageServiceLocalImplementaion implements ImageService{

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FileSystemStorageService storageService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BaseClient baseClient;

    @Override
    public CoverImage addBookImage(UUID uuid, MultipartFile file) throws BookException, ImageExistsException, InvalidFileException {
        String fileType = file.getContentType();
        if(!fileType.equals("image/jpeg") && !fileType.equals("image/png"))
        {
            throw new InvalidFileException("Only images of .png, .jpg or .jpeg formats are accepted");
        }
        Optional<Book> book = bookRepository.findById(uuid);
        if(book.isEmpty())
        {
            throw new BookException("Could not find book with id : "+uuid);
        }
        Optional<CoverImage> existingImage = imageRepository.findById(uuid);
        if(existingImage.isPresent())
        {
            throw new ImageExistsException("Image exists for book with id : "+uuid+". Update the book image using the PUT request.");
        }
        String uri = storageService.store(file);
        System.out.println("\n\nInsideImgServImpl Before baseClient.uploadFile(file, uuid)");
        //String uri = baseClient.uploadFile(file, uuid);
        System.out.println("\n\nInsideImgServImpl After baseClient.uploadFile(file, uuid)");
        CoverImage image = new CoverImage();
        image.setBook(book.get());
        image.setId(uuid);
        image.setUri(uri);
        imageRepository.save(image);
        return image;
    }

    @Override
    public CoverImage updateBookImage(UUID uuid, MultipartFile file) throws BookException, InvalidFileException {
        String fileType = file.getContentType();
        if(!fileType.equals("image/jpeg") && !fileType.equals("image/png"))
        {
            throw new InvalidFileException("Only images of .png, .jpg or .jpeg formats are accepted");
        }
        Optional<Book> result = bookRepository.findById(uuid);
        if(result.isEmpty())
        {
            throw new BookException("Could not find book with id : "+uuid);
        }
        Book book = result.get();

        storageService.deleteFile(book.getCoverImage().getUri());
        //String deleteMessage = baseClient.deleteFile(book.getCoverImage().getUri());
        //System.out.println("\n\n***"+deleteMessage);

        String uri = storageService.store(file);
        //String uri = baseClient.uploadFile(file, uuid);

        CoverImage image = new CoverImage();
        image.setBook(result.get());
        image.setId(uuid);
        image.setUri(uri);
        imageRepository.save(image);
        return image;
    }

    @Override
    public void deleteBookImage(UUID bookid) throws BookException, InvalidFileException {
        Optional<Book> result = bookRepository.findById(bookid);
        if (result.isEmpty()) {
            throw new BookException("Could not find book with id : " + bookid);
        }
        Book book = result.get();
        imageRepository.deleteById(book.getCoverImage().getId());
        storageService.deleteFile(book.getCoverImage().getUri());
        //String deleteMessage = baseClient.deleteFile(book.getCoverImage().getUri());
        //System.out.println("\n\n***"+deleteMessage);

    }
}
