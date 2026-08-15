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
    public ResponseEntity<String> shortenUrl(@RequestParam String longUrl) {
        try {
            return new ResponseEntity<>(urlShortenerService.shortenUrl(longUrl), HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Long URL already exists: " + longUrl, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>("Error occurred while shortening the URL: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable String shortUrl) {
        try {
            String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);

            if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
                originalUrl = "http://" + originalUrl;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(originalUrl));

            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
