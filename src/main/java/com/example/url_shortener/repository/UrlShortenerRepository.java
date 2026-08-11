package com.example.url_shortener.repository;

import com.example.url_shortener.model.entity.UrlShortenerEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortenerEntity, Long> {
    UrlShortenerEntity findByShortUrl(@NotNull String shortUrl);
}
