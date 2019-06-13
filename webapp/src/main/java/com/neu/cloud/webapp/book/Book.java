package com.neu.cloud.webapp.book;

import com.neu.cloud.webapp.filestorage.Image;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="Books")
@Getter
@Setter
public class Book {

    @Id
    @Column(name="UUID")
    private UUID uuid=UUID.randomUUID();

    @Column(name="Title")
    private String title;

    @Column(name="Author")
    private String author;

    @Column(name="ISBN")
    private String isbn;

    @Column(name="Quantity")
    private Integer quantity;

    @OneToOne( cascade  = CascadeType.ALL)
    @JoinColumn(name="BookImageID" , referencedColumnName = "ImageID")
    private Image image;

    public Book() {
    }

    public Book(UUID uuid, String title, String author, String isbn, Integer quantity ) {
        this.uuid = uuid;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
    }

}
