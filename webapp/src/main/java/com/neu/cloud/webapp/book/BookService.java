package com.neu.cloud.webapp.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book getBookById(UUID uuid){
        try{
            return bookRepository.findById(uuid).get();
        }
        catch(Exception ex){
            return null;
        }

    }

    public void deleteBookById(UUID uuid){
        bookRepository.deleteById(uuid);
    }


    public void saveAndUpdate(Book book) {
        bookRepository.save(book);
    }
}
