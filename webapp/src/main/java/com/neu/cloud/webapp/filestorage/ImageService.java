package com.neu.cloud.webapp.filestorage;

import com.neu.cloud.webapp.book.Book;
import com.neu.cloud.webapp.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.UUID;


@Service
public class ImageService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ImageRepository imageRepository;

    public Image getImageById(UUID id) {
        Optional<Image> temp= imageRepository.findById(id);
        if(temp.isPresent()) return temp.get();
        return null;
    }

    public void add(Image image) {
        imageRepository.save(image);
    }

    public String copyImageToFolder(UUID idBook, MultipartFile file) throws Exception {
       // String path = "/Users/prabhpreetsingh/uploads/" + idBook + "_" + file.getOriginalFilename();
        String name=idBook + "_" + file.getOriginalFilename();
        String path = "/images/" + name;
        ClassPathResource classPathResource = new ClassPathResource(path);
        path=classPathResource.getPath();
        File temp = new File(path);
        temp.createNewFile();
        FileOutputStream fOutStream = new FileOutputStream(temp);
        fOutStream.write(file.getBytes());
        fOutStream.close();
        return path;
    }

    public void addCover(Book book, MultipartFile file) throws Exception{
        String path = copyImageToFolder(book.getUuid(), file);
        Image image = new Image(path);
        book.setImage(image);
        bookRepository.save(book);
    }


    public void updateImage(Book book, MultipartFile file, Image oldImage) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource(oldImage.getUrl());
        deleteExistingFile(classPathResource.getPath());
        String path = copyImageToFolder(book.getUuid(), file);
        oldImage.setUrl(path);
        imageRepository.save(oldImage);
    }

    public void deleteExistingFile(String existingFilePath){
        ClassPathResource classPathResource = new ClassPathResource(existingFilePath);
        File existingImage = new File(classPathResource.getPath());
        existingImage.delete();
    }

    public void deleteImageById(Book book, Image image){
        deleteExistingFile(image.getUrl());
        book.setImage(null);
        imageRepository.delete(image);
    }
}
