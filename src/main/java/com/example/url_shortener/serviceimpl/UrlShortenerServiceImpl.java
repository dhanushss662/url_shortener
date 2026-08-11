package com.example.url_shortener.serviceimpl;

import com.example.url_shortener.model.entity.UrlShortenerEntity;
import com.example.url_shortener.repository.UrlShortenerRepository;
import com.example.url_shortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private final UrlShortenerRepository urlShortenerRepository;
    private static final String BASE_URL = "http://short.ly/";
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 7;

    @Autowired
    public UrlShortenerServiceImpl(UrlShortenerRepository urlShortenerRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
    }


    @Override
    public String shortenUrl(String originalUrl) {
        try {
            UrlShortenerEntity urlShortenerEntity = new UrlShortenerEntity();
            urlShortenerEntity.setLongUrl(originalUrl);
            String shortUrl = generateRandomShortUrl();
            urlShortenerEntity.setShortUrl(BASE_URL + shortUrl);
            urlShortenerEntity.setCreatedDate(LocalDateTime.now());
            urlShortenerRepository.save(urlShortenerEntity);
            return urlShortenerEntity.getShortUrl();
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Long URL already exists: " + originalUrl);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while shortening the URL: " + e.getMessage());
        }
    }

    @Override
    public String getOriginalUrl(String shortUrl) {
        UrlShortenerEntity urlShortenerEntity = urlShortenerRepository.findByShortUrl(shortUrl);
        if (urlShortenerEntity != null) {
            return urlShortenerEntity.getLongUrl();
        }
        return "No original URL found for the provided short URL: " + shortUrl;
    }

    private String generateRandomShortUrl() {
        StringBuilder shortUrl = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = (int) (Math.random() * ALPHABET.length());
            shortUrl.append(ALPHABET.charAt(index));
        }
        return shortUrl.toString();
    }
}
