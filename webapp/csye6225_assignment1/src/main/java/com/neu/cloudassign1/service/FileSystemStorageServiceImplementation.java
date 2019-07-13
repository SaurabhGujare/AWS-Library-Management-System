package com.neu.cloudassign1.service;

import com.neu.cloudassign1.exception.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageServiceImplementation implements FileSystemStorageService {

    private final Path fileStorageLocation;

    public FileSystemStorageServiceImplementation()
    {
        String PathStore = System.getProperty("user.home");
        this.fileStorageLocation = Paths.get(PathStore+"/myappfiles/uploads")
                .toAbsolutePath().normalize();

        System.out.println("\n\n**********PAth is "+PathStore+"/resources/uploads");
        System.out.println("\n\n**********JAVA OPTS region is "+System.getProperty("spring_clientRegion_name"));
        System.out.println("\n\n**********JAVA OPTS region is "+System.getenv("spring_clientRegion_name"));

        try
        {
            Files.createDirectories(this.fileStorageLocation);
        }
        catch (Exception ex)
        {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String store(MultipartFile file)
    {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        try
        {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
        return targetLocation.toUri().toString();
    }

    @Override
    public void deleteFile(String url){

        System.out.println("*****FSImpl deleteUrl"+url);
        File file = new File(url.substring(7));
        System.out.println("*****FSImpl deleteFile"+file.getAbsolutePath());
        file.delete();

    }
}
