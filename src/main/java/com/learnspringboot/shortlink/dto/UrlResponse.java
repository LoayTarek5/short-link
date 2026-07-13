package com.learnspringboot.shortlink.dto;

import com.learnspringboot.shortlink.model.UrlMapping;

public record UrlResponse(Long id, String shortCode, String url) {

    public static UrlResponse from(UrlMapping mapping) {
        return new UrlResponse(mapping.getId(), mapping.getShortCode(), mapping.getUrl());
    }
}
