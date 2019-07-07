package com.neu.cloud.webapp.filestorage;

import com.neu.cloud.webapp.book.Book;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public interface ImageService {

    Image getPresignedUrl(UUID id);

    Image getImageById(UUID id);

    String copyImageToFolder(UUID idBook, MultipartFile file) throws Exception;

    void addCover(Book book, MultipartFile file) throws Exception;

    void updateImage(Book book, MultipartFile file, Image oldImage) throws Exception;

    void deleteExistingFile(String existingFilePath);

    void deleteImageById(Book book, Image image);
}
