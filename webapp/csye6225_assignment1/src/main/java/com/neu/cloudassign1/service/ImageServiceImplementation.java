package com.neu.cloudassign1.service;

import com.neu.cloudassign1.dao.BookDAO;
import com.neu.cloudassign1.dao.ImageDAO;
import com.neu.cloudassign1.model.CoverImage;
import com.neu.cloudassign1.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageServiceImplementation implements ImageService {

    private ImageDAO imageDAO;
    private EntityManager entityManager;
    private BookDAO bookDAO;

    @Autowired
    private ImageServiceImplementation(EntityManager entityManager, ImageDAO imageDAO, BookDAO bookDAO){
        this.entityManager = entityManager;
        this.imageDAO = imageDAO;
        this.bookDAO = bookDAO;

    }

    
	private String localPath = "/Home/Gaurav/images/";
	
	@Override
	public String uploadFile(MultipartFile multipartFile) throws Exception {
		
		byte[] bytes = multipartFile.getBytes();
        Path path = Paths.get(localPath + Utils.generateFileName(multipartFile));
        Files.write(path, bytes);
        
		return path.toString();
	}

	@Override
	public String deleteFile(String fileUrl) throws Exception {
		
		File file = new File(fileUrl);
		String result = "Delete attachment for transaction failed";
		if(file.delete()){
			result = file.getName() + " " + "Deleted Successfully";
		}
		
		return result;
		
	}

	@Override
	public void saveImage(String id, String uri) throws Exception {

//		Book book = bookDAO.getBookById(id);
		
		CoverImage image  = new CoverImage();
		image.setUri(uri);
		image.setBookId(id);
		imageDAO.saveCoverImage(image);
		
		
//		Book book = bookDAO.getBookById(id);
//		book.setCoverImage(image);
//		
//		bookDAO.updateBookForImage(book, book.getTitle());
		
	}

	@Override
	public List<CoverImage> getAllCoverImages() throws Exception {
		// TODO Auto-generated method stub
		return imageDAO.getAllCoverImage();
	}

	@Override
	public CoverImage getCoverImageByBookId(String id) throws Exception {
		// TODO Auto-generated method stub
		return imageDAO.getCoverImageByBookId(id);
	}
	
}
