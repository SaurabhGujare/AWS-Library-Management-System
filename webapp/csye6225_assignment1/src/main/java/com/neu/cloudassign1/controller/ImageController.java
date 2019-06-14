package com.neu.cloudassign1.controller;

import com.neu.cloudassign1.service.ImageService;
import com.neu.cloudassign1.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ImageController {
	
	private ImageService imageService;
	
	@Autowired
    public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	@RequestMapping(value = "/book/{id}/image", method = RequestMethod.POST)
	public ResponseEntity uploadImage(@PathVariable String id, @RequestPart(value = "file") MultipartFile file) throws Exception {
    	
        Map<String,String> bookMap = new HashMap<>();
        
		if(Utils.isValidExt(file))
		{	
			try {
				String uri = imageService.uploadFile(file);				
				imageService.saveImage(id, uri);				
	
	            bookMap.clear();
	            bookMap.put("successMessage","Image saved successfully");
	            return new ResponseEntity(bookMap, HttpStatus.OK);
			}
			catch (Exception e) {
	            bookMap.clear();
	            bookMap.put("errorMessage", "Unable to save image for book with id " + id );
	            return new ResponseEntity(bookMap, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
            bookMap.clear();
            bookMap.put("errorMessage","Unable to save Image");
            return new ResponseEntity(bookMap, HttpStatus.UNAUTHORIZED);	
			
		}
        
            
    }

}
