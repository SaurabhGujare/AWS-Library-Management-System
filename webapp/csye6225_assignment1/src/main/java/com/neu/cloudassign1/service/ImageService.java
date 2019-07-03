package com.neu.cloudassign1.service;

import com.neu.cloudassign1.exception.BookException;
import com.neu.cloudassign1.exception.ImageExistsException;
import com.neu.cloudassign1.exception.InvalidFileException;
import com.neu.cloudassign1.model.CoverImage;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.UUID;

public interface ImageService {
    public CoverImage addBookImage(UUID uuid, MultipartFile image) throws BookException, ImageExistsException, InvalidFileException;
    public CoverImage updateBookImage(UUID uuid, MultipartFile file) throws BookException, InvalidFileException;
    public void deleteBookImage(UUID bookid) throws BookException, InvalidFileException;

}
