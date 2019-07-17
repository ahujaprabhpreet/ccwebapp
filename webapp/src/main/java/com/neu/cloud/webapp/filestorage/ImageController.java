package com.neu.cloud.webapp.filestorage;

import com.neu.cloud.webapp.book.Book;
import com.neu.cloud.webapp.book.BookService;
import com.neu.cloud.webapp.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/bookpp/{idBook}/image")
public class ImageController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<?> postImage(@PathVariable UUID idBook, @RequestParam(required = false) MultipartFile file) throws Exception{
        if(file == null) return ResponseEntity.badRequest().body(new CustomResponse(new Date(),"Select a file",""));

        if(!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg"))
            return ResponseEntity.badRequest().body(new CustomResponse(new Date(),"Wrong File Format",""));

        Book book = bookService.getBookById(idBook);
        if(book == null) return new ResponseEntity(new CustomResponse(new Date(),"Book Not Found",""),HttpStatus.NOT_FOUND);

        if(book.getImage() != null) return new ResponseEntity(new CustomResponse(new Date(),"Image already Exists!",""),HttpStatus.CONFLICT);
        imageService.addCover(book, file);
        return ResponseEntity.ok().body(book.getImage());
    }

    @GetMapping("/{idImage}")
    public ResponseEntity<?> getBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage){

        Book book = bookService.getBookById(idBook);
        if(book == null) return new ResponseEntity(new CustomResponse(new Date(),"Book Not Found",""),HttpStatus.NOT_FOUND);


        Image bookImage = imageService.getPresignedUrl(idImage);
        if(bookImage == null) return new ResponseEntity(new CustomResponse(new Date(),"Image Not Found",""),HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(bookImage);

    }

    @PutMapping("/{idImage}")
    public ResponseEntity<?> updateBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage,
                                             @RequestParam(required = false) MultipartFile file) throws Exception{

        if(file == null) return ResponseEntity.badRequest().body(new CustomResponse(new Date(),"Select a file",""));

        Book book = bookService.getBookById(idBook);
        if(book == null) return new ResponseEntity(new CustomResponse(new Date(),"Book Not Found",""),HttpStatus.NOT_FOUND);


        if(book.getImage() == null) return new ResponseEntity(new CustomResponse(new Date(),"No existing Image found to update",""),HttpStatus.NOT_FOUND);


        if(!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpg") && !file.getContentType().equals("image/jpeg"))
            return ResponseEntity.badRequest().body(new CustomResponse(new Date(),"Wrong File Format",""));

        Image existingImage = imageService.getImageById(idImage);
        imageService.updateImage(book, file, existingImage);

        return  ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{idImage}")
    public ResponseEntity<?> deleteBookImage(@PathVariable UUID idBook, @PathVariable UUID idImage){

        Book book = bookService.getBookById(idBook);
        if(book == null) return new ResponseEntity(new CustomResponse(new Date(),"Book Not Found",""),HttpStatus.NOT_FOUND);

        Image image = imageService.getImageById(idImage);
        if(image == null) return new ResponseEntity(new CustomResponse(new Date(),"Image Not Found",""),HttpStatus.NOT_FOUND);

        imageService.deleteImageById(book, image);
        return  ResponseEntity.noContent().build();

    }

}