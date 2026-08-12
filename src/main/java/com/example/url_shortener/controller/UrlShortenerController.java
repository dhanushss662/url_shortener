package com.example.url_shortener.controller;

import com.example.url_shortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/url-shortener")
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String longUrl) {
        try {
            return urlShortenerService.shortenUrl(longUrl);
        }catch (IllegalArgumentException e) {
            return "Long URL already exists: " + longUrl;
        }catch (Exception e) {
            return "Error occurred while shortening the URL: " + e.getMessage();
        }
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> redirectToLongUrl(@RequestParam String shortUrl) {
        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
