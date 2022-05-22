package com.vinceruby.urlshortener.controller;

import com.vinceruby.urlshortener.contract.service.ShortUrlService;
import com.vinceruby.urlshortener.domain.ShortUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.format.DateTimeFormatter;

@RequestMapping("/short-urls")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ShortUrlController {

    @AllArgsConstructor
    @Builder
    @Getter
    @NoArgsConstructor
    @Setter
    public static class CreateShortUrl {

        private String destination;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @NoArgsConstructor
    @Setter
    public static class ShortUrlResponse {

        private String id;
        private String code;
        private String destination;
        private String createdAt;
        private String updatedAt;

        public static ShortUrlResponse fromDomain(ShortUrl shortUrl) {
            return ShortUrlResponse.builder()
                    .id(shortUrl.getId().toString())
                    .code(shortUrl.getCode())
                    .destination(shortUrl.getDestination().toString())
                    .createdAt(shortUrl.getCreatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    .updatedAt(shortUrl.getUpdatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    .build();
        }
    }

    private final ShortUrlService shortUrlService;

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ShortUrlResponse create(@RequestBody CreateShortUrl input) {
        log.debug("Attempting to create new short url for [{}]", input.getDestination());

        ShortUrl shortUrl = shortUrlService.create(URI.create(input.getDestination()));

        log.debug("Created short url for [{}] with code [{}]", shortUrl.getDestination(), shortUrl.getCode());

        return ShortUrlResponse.fromDomain(shortUrl);
    }
}
