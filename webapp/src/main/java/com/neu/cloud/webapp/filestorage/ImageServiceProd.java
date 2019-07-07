package com.neu.cloud.webapp.filestorage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.neu.cloud.webapp.book.Book;
import com.neu.cloud.webapp.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("prod")
public class ImageServiceProd implements ImageService{

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BookRepository bookRepository;

    private static final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .build();

    @Value("${BUCKET_NAME}")
    private String bucket_name;

    private String path = "";

    public File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    @Override
    public Image getPresignedUrl(UUID id) {

        Image image = getImageById(id);

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

            return new Image(image.getId(), url.toString());
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Image getImageById(UUID id) {
        Optional<Image> temp = imageRepository.findById(id);
        return temp.isPresent() ? temp.get() : null;
    }

    @Override
    public String copyImageToFolder(UUID idBook, MultipartFile file) throws Exception{
        path = file.getOriginalFilename();

        try {
            System.out.println(bucket_name);
            s3.putObject(bucket_name, path, multipartToFile(file, file.getOriginalFilename()));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        return path;
    }

    @Override
    public void addCover(Book book, MultipartFile file) throws Exception {
        String path = copyImageToFolder(book.getUuid(), file);
        Image image = new Image(path);
        book.setImage(image);
        bookRepository.save(book);
    }

    @Override
    public void updateImage(Book book, MultipartFile file, Image oldImage) throws Exception {
        deleteExistingFile(oldImage.getUrl());
        String path = copyImageToFolder(book.getUuid(), file);
        oldImage.setUrl(path);
        imageRepository.save(oldImage);
    }

    @Override
    public void deleteExistingFile(String existingFilePath) {
        try {
            s3.deleteObject(bucket_name, existingFilePath);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }

    @Override
    public void deleteImageById(Book book, Image image) {
        deleteExistingFile(image.getUrl());
        book.setImage(null);
        imageRepository.delete(image);
    }
}
