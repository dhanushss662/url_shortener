package com.example.url_shortener.service;

public interface UrlShortenerService {

    String shortenUrl(String originalUrl);

    String getOriginalUrl(String shortUrl);
}
