package com.learnspringboot.shortlink.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateUrlRequest(
        @NotBlank(message = "URL must not be blank")
        @URL(message = "URL must be valid")
        String url
) {
}