package com.neu.cloudassign1.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class AmazonClient implements BaseClient {

    private static final String DIRECTORY = "CoverImage/";
    private static final String UNDERSCORE = "_";

    @Value("${spring.bucket.name}")
    private String bucketName;

    private AmazonS3 s3client;

    @PostConstruct
    private void initializeAmazon() {
        this.s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, UUID bookId) {
        //String fileName = bookId+UNDERSCORE+java.time.LocalDateTime.now().toString();
        String fileName = bookId+UNDERSCORE+StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println("***\n\nfileName"+fileName+"\n\nbucketName"+bucketName);
        String s3Path = DIRECTORY+fileName;

        System.out.println("***\n\ns3Path"+s3Path+"\n\nbucketName"+bucketName);

        InputStream inputStream = null;
        try{
            inputStream = multipartFile.getInputStream();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        s3client.putObject(bucketName, s3Path, inputStream, new ObjectMetadata());
        return s3Path;
    }

    @Override
    public String deleteFile(String fileUrl) {
        System.out.println("*****FSImpl deleteUrl"+fileUrl);
        File file = new File(fileUrl);
        System.out.println("*****FSImpl deleteFile"+file.getAbsolutePath());
        System.out.println("*****FSImpl deleteFile2"+fileUrl);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileUrl));
        return "File Deleted Successfully";
    }
}
