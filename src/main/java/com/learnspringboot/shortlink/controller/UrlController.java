package com.learnspringboot.shortlink.controller;

import com.learnspringboot.shortlink.dto.CreateUrlRequest;
import com.learnspringboot.shortlink.dto.UrlResponse;
import com.learnspringboot.shortlink.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UrlResponse create(@Valid @RequestBody CreateUrlRequest request) {
        return UrlResponse.from(service.create(request.url()));
    }

    @GetMapping("/{shortCode}")
    public UrlResponse get(@PathVariable String shortCode) {
        String url = service.findUrlByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException(shortCode));
        return new UrlResponse(null, shortCode, url);
    }

    @GetMapping
    public List<UrlResponse> list() {
        return service.findAll().stream().map(UrlResponse::from).toList();
    }
}