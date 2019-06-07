package com.neu.cloud.webapp.book;

import com.neu.cloud.webapp.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@RequestBody Book book){

        if(book.getAuthor()==null || book.getQuantity()==null ||
                book.getTitle()==null || book.getIsbn()==null)
            return new ResponseEntity(new CustomResponse(new Date(),"Fields cannot be null","" ), HttpStatus.BAD_REQUEST);

        bookRepository.save(book);

        return  new ResponseEntity(book, HttpStatus.CREATED);

    }

    @GetMapping("/book")
    public ResponseEntity getAllBooks(){

        List<Book> books = bookRepository.findAll();
        return  new ResponseEntity(books, HttpStatus.OK);

    }

    @GetMapping("/book/{uuid}")
    public ResponseEntity<Book> getBook(@PathVariable UUID uuid){

        Optional<Book> book=bookRepository.findById(uuid);

        if(!book.isPresent())
            return new ResponseEntity(new CustomResponse(new Date(),"id not found","" ),HttpStatus.NOT_FOUND);

        return  ResponseEntity.ok(book.get());

    }
}
