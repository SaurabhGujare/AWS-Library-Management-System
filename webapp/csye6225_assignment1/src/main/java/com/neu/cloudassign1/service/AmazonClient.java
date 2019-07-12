package com.neu.cloudassign1.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;


@Service
public class AmazonClient implements BaseClient {

    private static final String DIRECTORY = "CoverImage/";
    private static final String UNDERSCORE = "_";
    private String objectKey;

    @Value("${spring_bucket_name}")
    private String bucketName = System.getProperty("spring_bucket_name");

    @Value("${spring_clientRegion_name}")
    private String clientRegion = System.getProperty("spring_clientRegion_name");

    private AmazonS3 s3Client;

    @PostConstruct
    private void initializeAmazon() {
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new InstanceProfileCredentialsProvider(false))
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
        s3Client.putObject(bucketName, s3Path, inputStream, new ObjectMetadata());
        return s3Client.getUrl(bucketName,s3Path).toExternalForm();
    }

    @Override
    public String deleteFile(String fileUrl) {
        System.out.println("*****S3Bucket Image URL: "+fileUrl);

        //File file = new File(fileUrl);
        //System.out.println("*****FSImpl deleteFile"+file.getAbsolutePath());
        System.out.println("*****S3Bucket Image Key: "+fileUrl.substring(fileUrl.lastIndexOf("/C")+1));
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileUrl.substring(fileUrl.lastIndexOf("/C")+1)));
        return "File Deleted Successfully";
    }

    @Override
    public URL GeneratePresignedURL(String objectKey) {
        this.objectKey = objectKey;
        try {

            // Set the presigned URL to expire after 2 mins.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 2 *60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            System.out.println("Pre-Signed URL: " + url.toString());
            return url;
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return null;
    }
    

}
