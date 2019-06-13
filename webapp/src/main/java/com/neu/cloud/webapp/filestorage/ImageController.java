package com.neu.cloud.webapp.filestorage;

import com.neu.cloud.webapp.book.Book;
import com.neu.cloud.webapp.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RestController
@RequestMapping("/book/{idBook}/image")
public class ImageController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<?> postImage(@PathVariable UUID idBook, @RequestParam(required = false) MultipartFile file) throws Exception{
        if(file == null) return ResponseEntity.badRequest().body("Select a file");

        if(!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg"))
            return ResponseEntity.badRequest().body("Wrong File Format");

        Book book = bookService.getBookById(idBook);
        if(book == null) return ResponseEntity.badRequest().body("Wrong Book_ID");

        if(book.getImage() != null) return ResponseEntity.badRequest().body("Image already Exists!");
        imageService.addCover(book, file);

        return ResponseEntity.ok().body(book.getImage());
    }

    @GetMapping("/{idImage}")
    public ResponseEntity<?> getBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage){

        Book book = bookService.getBookById(idBook);
        if(book == null) return ResponseEntity.badRequest().body("Wrong Book_ID");

        Image bookImage = imageService.getImageById(idImage);
        if(bookImage == null) return ResponseEntity.badRequest().body("Book Image not Found");

        return ResponseEntity.ok().body(bookImage);

    }

    @PutMapping("/{idImage}")
    public ResponseEntity<?> updateBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage,
                                             @RequestParam(required = false) MultipartFile file) throws Exception{

        if(file == null) return ResponseEntity.badRequest().body("Select a file");

        Book book = bookService.getBookById(idBook);
        if(book == null) return ResponseEntity.badRequest().body("Wrong Book_ID");

        if(book.getImage() == null) return ResponseEntity.badRequest().body("No existing Image found to update");

        if(!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg"))
            return ResponseEntity.badRequest().body("Wrong File Format");

        Image existingImage = imageService.getImageById(idImage);
        imageService.updateImage(book, file, existingImage);

        return  ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{idImage}")
    public ResponseEntity<?> deleteBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage){

        Book book = bookService.getBookById(idBook);
        if(book == null) return ResponseEntity.badRequest().body("Wrong Book_ID");

        Image image = imageService.getImageById(idImage);
        if(image == null) return ResponseEntity.badRequest().body("Image not found");

        imageService.deleteImageById(book, image);
        return  ResponseEntity.noContent().build();

    }

}