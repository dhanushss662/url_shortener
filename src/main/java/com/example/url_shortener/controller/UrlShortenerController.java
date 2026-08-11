package com.example.url_shortener.controller;

import com.example.url_shortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String redirectToLongUrl(@RequestParam String shortUrl) {
        return urlShortenerService.getOriginalUrl(shortUrl);
    }
}
