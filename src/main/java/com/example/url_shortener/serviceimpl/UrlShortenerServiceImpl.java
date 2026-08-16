package com.example.url_shortener.serviceimpl;

import com.example.url_shortener.model.entity.UrlShortenerEntity;
import com.example.url_shortener.repository.UrlShortenerRepository;
import com.example.url_shortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.url_shortener.serviceimpl.ConvertToBase62.convertToBase62;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private static final String BASE_URL = "http://short.ly/";
    private final UrlShortenerRepository urlShortenerRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UrlShortenerServiceImpl(UrlShortenerRepository urlShortenerRepository, RedisTemplate<String, String> redisTemplate) {
        this.urlShortenerRepository = urlShortenerRepository;
        this.redisTemplate = redisTemplate;

    }

    @Override
    public String shortenUrl(String originalUrl) {
        try {
            // Check if URL already exists
            Optional<UrlShortenerEntity> existing = urlShortenerRepository.findByLongUrl(originalUrl);
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Long URL already exists: " + BASE_URL+existing.get().getShortUrl());
            }

            UrlShortenerEntity entity = new UrlShortenerEntity();
            entity.setLongUrl(originalUrl);
            entity.setCreatedDate(LocalDateTime.now());
            System.out.println("Entity before saving: " + entity);
            urlShortenerRepository.save(entity);
            long id = entity.getId();
            String base62 = convertToBase62(id);

            if (base62.length() < 7) {
                base62 = String.format("%7s", base62).replace(' ', '0');
            }
            entity.setShortUrl(base62);
            redisTemplate.opsForValue().set(base62, originalUrl);
            urlShortenerRepository.save(entity);

            return BASE_URL + base62;
        } catch (IllegalArgumentException e) {
            throw e; // rethrow with same message
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while shortening the URL: " + e.getMessage());
        }
    }

    @Override
    public String getOriginalUrl(String shortUrl) {
        String originalUrl = redisTemplate.opsForValue().get(shortUrl);
        if (originalUrl != null) {
            return originalUrl;
        }
        UrlShortenerEntity entity = urlShortenerRepository.findByShortUrl(shortUrl);
        if (entity == null) {
            throw new IllegalArgumentException("No original URL found for short code: " + shortUrl);
        }

        return entity.getLongUrl();
    }
}
