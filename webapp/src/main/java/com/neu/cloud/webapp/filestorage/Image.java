package com.neu.cloud.webapp.filestorage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="Files")
@Getter
@Setter
public class Image {

    @Id
    @Column(name = "ImageID")
    private UUID id = UUID.randomUUID();

    @Column(name = "Url")
    private String url;

    public Image() {
    }

    public Image(String url) {
        this.url = url;
    }
}

