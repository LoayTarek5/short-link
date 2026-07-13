package com.learnspringboot.shortlink.service;

import com.learnspringboot.shortlink.model.UrlMapping;
import com.learnspringboot.shortlink.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlService {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final Duration CACHE_TTL = Duration.ofHours(1);
    private static final String CACHE_PREFIX = "url:";

    private final UrlMappingRepository repository;
    private final StringRedisTemplate redisTemplate;
    private final SecureRandom random = new SecureRandom();

    public UrlMapping create(String url) {
        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(generateUniqueCode());
        mapping.setUrl(url);
        return repository.save(mapping);
    }

    public Optional<String> findUrlByShortCode(String shortCode) {
        String cacheKey = CACHE_PREFIX + shortCode;

        String cachedUrl = redisTemplate.opsForValue().get(cacheKey);
        if (cachedUrl != null) {
            return Optional.of(cachedUrl);
        }

        Optional<String> url = repository.findByShortCode(shortCode).map(UrlMapping::getUrl);
        url.ifPresent(value -> redisTemplate.opsForValue().set(cacheKey, value, CACHE_TTL));
        return url;
    }

    public List<UrlMapping> findAll() {
        return repository.findAll();
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = randomCode();
        } while (repository.existsByShortCode(code));
        return code;
    }

    private String randomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}