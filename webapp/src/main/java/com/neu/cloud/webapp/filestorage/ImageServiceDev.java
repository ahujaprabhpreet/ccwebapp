package com.neu.cloud.webapp.filestorage;

import com.neu.cloud.webapp.book.Book;
import com.neu.cloud.webapp.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("dev")
public class ImageServiceDev implements ImageService{

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BookRepository bookRepository;

    private String path = "";

    @Override
    public Image getPresignedUrl(UUID id) {
        Image image = getImageById(id);
        return image;
    }

    @Override
    public Image getImageById(UUID id) {
        Optional<Image> temp = imageRepository.findById(id);
        return temp.isPresent() ? temp.get() : null;
    }

    @Override
    public String copyImageToFolder(UUID idBook, MultipartFile file) throws Exception{
        String name = idBook + "_" + file.getOriginalFilename();
        System.out.println(name);
        path = System.getProperty("user.home") + "/images/+" + name;
        System.out.println(path);
        File temp = new File(path);
        temp.createNewFile();
        FileOutputStream fOutStream = new FileOutputStream(temp);
        fOutStream.write(file.getBytes());
        fOutStream.close();
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
        File existingImage = new File(existingFilePath);
        existingImage.delete();
    }

    @Override
    public void deleteImageById(Book book, Image image) {
        deleteExistingFile(image.getUrl());
        book.setImage(null);
        imageRepository.delete(image);
    }
}
