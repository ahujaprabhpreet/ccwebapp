package com.neu.cloud.webapp.book;

import com.neu.cloud.webapp.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
}
