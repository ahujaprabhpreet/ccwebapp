package com.neu.cloud.webapp.filestorage;

import com.neu.cloud.webapp.book.Book;
import com.neu.cloud.webapp.book.BookService;
import com.neu.cloud.webapp.response.CustomResponse;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/book/{idBook}/image")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private StatsDClient statsDClient;

    @PostMapping
    public ResponseEntity<?> postImage(@PathVariable UUID idBook, @RequestParam(required = false) MultipartFile file) throws Exception{

        statsDClient.incrementCounter("endpoint.image.create.http.post");

        if(file == null){
            logger.warn("No file selected");
            return ResponseEntity.badRequest().body(new CustomResponse(new Date(),"Select a file",""));
        }

        if(!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg")){
            logger.warn("Wrong File Format for image");
            return ResponseEntity.badRequest().body(new CustomResponse(new Date(),"Wrong File Format",""));
        }

        Book book = bookService.getBookById(idBook);
        if(book == null){
            logger.warn("Book not found");
            return new ResponseEntity(new CustomResponse(new Date(),"Book Not Found",""),HttpStatus.NOT_FOUND);
        }

        if(book.getImage() != null) {
            logger.warn("Image for the book already Exists!");
            return new ResponseEntity(new CustomResponse(new Date(),"Image already Exists!",""),HttpStatus.CONFLICT);
        }
        imageService.addCover(book, file);
        logger.warn("Image for the book added!");
        return ResponseEntity.ok().body(book.getImage());
    }

    @GetMapping("/{idImage}")
    public ResponseEntity<?> getBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage){

        statsDClient.incrementCounter("endpoint.image.getOne.http.get");

        Book book = bookService.getBookById(idBook);
        if(book == null){
            logger.warn("Book Not Found!");
            return new ResponseEntity(new CustomResponse(new Date(),"Book Not Found",""),HttpStatus.NOT_FOUND);
        }


        Image bookImage = imageService.getPresignedUrl(idImage);
        if(bookImage == null) {
            logger.warn("Image Not Found!");
            return new ResponseEntity(new CustomResponse(new Date(),"Image Not Found",""),HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok().body(bookImage);

    }

    @PutMapping("/{idImage}")
    public ResponseEntity<?> updateBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage,
                                             @RequestParam(required = false) MultipartFile file) throws Exception{

        statsDClient.incrementCounter("endpoint.image.update.http.put");

        if(file == null) {
            logger.warn("File Not selected!");
            return ResponseEntity.badRequest().body(new CustomResponse(new Date(),"Select a file",""));
        }

        Book book = bookService.getBookById(idBook);

        if(book == null){
            logger.warn("Book Not Found");
            return new ResponseEntity(new CustomResponse(new Date(),"Book Not Found",""),HttpStatus.NOT_FOUND);
        }


        if(book.getImage() == null){
            logger.warn("No existing Image found to update");
            return new ResponseEntity(new CustomResponse(new Date(),"No existing Image found to update",""),HttpStatus.NOT_FOUND);
        }


        if(!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg")){
            logger.warn("Wrong File Format!");
            return ResponseEntity.badRequest().body(new CustomResponse(new Date(),"Wrong File Format",""));
        }

        Image existingImage = imageService.getImageById(idImage);
        imageService.updateImage(book, file, existingImage);

        logger.info("Book Image updated!");
        return  ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{idImage}")
    public ResponseEntity<?> deleteBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage){

        statsDClient.incrementCounter("endpoint.image.deleteOne.http.delete");

        Book book = bookService.getBookById(idBook);

        if(book == null){
            logger.warn("Book Not Found");
            return new ResponseEntity(new CustomResponse(new Date(),"Book Not Found",""),HttpStatus.NOT_FOUND);
        }

        Image image = imageService.getImageById(idImage);
        if(image == null){
            logger.warn("Image Not Found!");
            return new ResponseEntity(new CustomResponse(new Date(),"Image Not Found",""),HttpStatus.NOT_FOUND);
        }

        imageService.deleteImageById(book, image);
        logger.info("Book Image deleted!");
        return  ResponseEntity.noContent().build();

    }

}