package com.neu.cloud.webapp;

import com.neu.cloud.webapp.book.Book;
import com.neu.cloud.webapp.book.BookRepository;
import com.neu.cloud.webapp.book.BookService;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
public class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;

    private static Book BOOK;

    @Before
    public void initialSetup(){
        this.BOOK = new Book(UUID.randomUUID(), "Math", "MathAuthor", "MathISBN", 10);
    }

    @Test
    public void test_BookGetByID(){
        Mockito.when(bookRepository.findById(BOOK.getUuid())).thenReturn(Optional.of(BOOK));
        Book book = bookService.getBookById(BOOK.getUuid());
        Assertions.assertThat(book).isEqualTo(BOOK);
    }

    @Test
    public void test_BookDeleteByID(){
        bookService.deleteBookById(BOOK.getUuid());
        Mockito.verify(bookRepository).deleteById(BOOK.getUuid());
    }
}
