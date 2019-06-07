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
            return new ResponseEntity(new CustomResponse(new Date(),"Incorrect Book Details","title, author, isbn, quantity" ), HttpStatus.BAD_REQUEST);

        bookRepository.save(book);

        return  new ResponseEntity(book, HttpStatus.CREATED);

    }

    @GetMapping("/book")
    public ResponseEntity getAllBooks(){

        List<Book> books = bookRepository.findAll();
        return  new ResponseEntity(books, HttpStatus.OK);

    }

    @GetMapping("/book/{suuid}")
    public ResponseEntity<Book> getBook(@PathVariable String suuid){

        UUID uuid = UUID.randomUUID();

        if(!checkUuid(suuid))
            return new ResponseEntity(new CustomResponse(new Date(),"ID must be of type UUID","" ),HttpStatus.BAD_REQUEST);

        uuid=UUID.fromString(suuid);
        Optional<Book> book=bookRepository.findById(uuid);

        if(!book.isPresent())
            return new ResponseEntity(new CustomResponse(new Date(),"Book not found","" ),HttpStatus.NOT_FOUND);

        return  ResponseEntity.ok(book.get());

    }

    @DeleteMapping("/book/{suuid}")
    public ResponseEntity<Void> deleteBook(@PathVariable String suuid){
        UUID uuid = UUID.randomUUID();
        if(!checkUuid(suuid))
            return new ResponseEntity(new CustomResponse(new Date(),"ID must be of type UUID","" ),HttpStatus.BAD_REQUEST);

        uuid=UUID.fromString(suuid);
        Optional<Book> book=bookRepository.findById(uuid);

        if(!book.isPresent())
            return new ResponseEntity(new CustomResponse(new Date(),"Book not found","" ),HttpStatus.NOT_FOUND);

        bookRepository.deleteById(uuid);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/book/{suuid}")
    public ResponseEntity<Void> updateBook(@RequestBody Book book, @PathVariable String suuid){

        UUID uuid = UUID.randomUUID();
        if(!checkUuid(suuid))
            return new ResponseEntity(new CustomResponse(new Date(),"ID must be of type UUID","" ),HttpStatus.BAD_REQUEST);

        uuid=UUID.fromString(suuid);

        Optional<Book> fbook=bookRepository.findById(uuid);

        if(!fbook.isPresent())
            return new ResponseEntity(new CustomResponse(new Date(),"Book not found","" ),HttpStatus.NOT_FOUND);

        if(book.getAuthor()==null || book.getQuantity()==null ||
                book.getTitle()==null || book.getIsbn()==null)
            return new ResponseEntity(new CustomResponse(new Date(),"Incorrect Book Details","title, author, isbn, quantity" ), HttpStatus.BAD_REQUEST);

        book.setUuid(uuid);
        bookRepository.save(book);
        return  ResponseEntity.noContent().build();

    }

    public boolean checkUuid(String uuid){
        try{
            UUID nuuid = UUID.fromString(uuid);
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }
}
