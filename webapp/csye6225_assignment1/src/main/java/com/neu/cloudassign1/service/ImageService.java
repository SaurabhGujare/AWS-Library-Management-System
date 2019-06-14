package com.neu.cloudassign1.service;

import com.neu.cloudassign1.model.CoverImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
	
	public String uploadFile(MultipartFile multipartFile) throws Exception;
	
	public String deleteFile(String fileUrl) throws Exception;
	
	public void saveImage(String id, String path) throws Exception;
	
	public List<CoverImage> getAllCoverImages() throws Exception;
	
	public CoverImage getCoverImageByBookId(String id) throws Exception;

}
