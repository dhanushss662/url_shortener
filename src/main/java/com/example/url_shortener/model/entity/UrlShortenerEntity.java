package com.example.url_shortener.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "longurl_shorturl")
public class UrlShortenerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "long_url", nullable = false, unique = true)
    private String longUrl;
    @Column(name = "short_url", unique = true)
    private String shortUrl;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
