package com.neu.cloud.webapp.filestorage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.neu.cloud.webapp.book.Book;
import com.neu.cloud.webapp.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;


@Service
public class ImageService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private Environment environment;

    private static final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new ProfileCredentialsProvider())
            .withRegion("us-east-1").build();

    private String path = "";
    private Bucket bucket = s3.listBuckets().get(0);
    private String bucket_name = bucket.getName();

    public Image getPresignedUrl(UUID id) {

        Image image = getImageById(id);

//      Check if Active profiles contains "local" or "test"
        if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("dev"))))
        {
            return image;
        }

        //Check if Active profiles contains "prod"
        else if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prod")) ))
        {
            try {

                // Set the presigned URL to expire after two minutes.
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = expiration.getTime();
                expTimeMillis += 1000 * 60 * 2;
                expiration.setTime(expTimeMillis);

                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucket_name, image.getUrl())
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);

                return new Image(image.getId(),url.toString());
            }
            catch(AmazonServiceException e) {
                e.printStackTrace();
            }
            catch(SdkClientException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Image getImageById(UUID id) {
        Optional<Image> temp= imageRepository.findById(id);
        return temp.isPresent() ? temp.get() : null;
    }

    public void add(Image image) {
        imageRepository.save(image);
    }

    public String copyImageToFolder(UUID idBook, MultipartFile file) throws Exception {

        //Check if Active profiles contains "local" or "test"
        if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("dev"))))
        {
            String name=idBook + "_" + file.getOriginalFilename();
            path = System.getProperty("user.home")+ "/images/+" +name;
            File temp = new File(path);
            temp.createNewFile();
            FileOutputStream fOutStream = new FileOutputStream(temp);
            fOutStream.write(file.getBytes());
            fOutStream.close();
            return path;
        }

        //Check if Active profiles contains "prod"
        else if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prod")) ))
        {
            path = file.getOriginalFilename();

            try {
                s3.putObject(bucket_name, path, multipartToFile(file, file.getOriginalFilename()));
            }

            catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        }

        return path;
    }

    public static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    public void addCover(Book book, MultipartFile file) throws Exception{
        String path = copyImageToFolder(book.getUuid(), file);
        Image image = new Image(path);
        book.setImage(image);
        bookRepository.save(book);
    }


    public void updateImage(Book book, MultipartFile file, Image oldImage) throws Exception {
        deleteExistingFile(oldImage.getUrl());
        String path = copyImageToFolder(book.getUuid(), file);
        oldImage.setUrl(path);
        imageRepository.save(oldImage);
    }

    public void deleteExistingFile(String existingFilePath){

        //Check if Active profiles contains "dev"
        if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("dev"))))
        {
            File existingImage = new File(existingFilePath);
            existingImage.delete();
        }

        //Check if Active profiles contains "prod"
        else if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("prod")) ))
        {
            try {
                s3.deleteObject(bucket_name, existingFilePath);
            }
            catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        }
    }

    public void deleteImageById(Book book, Image image){
        deleteExistingFile(image.getUrl());
        book.setImage(null);
        imageRepository.delete(image);
    }
}
