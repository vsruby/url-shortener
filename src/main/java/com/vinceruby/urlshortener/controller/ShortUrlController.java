package com.vinceruby.urlshortener.controller;

import com.vinceruby.urlshortener.contract.service.ShortUrlService;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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

    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlService shortUrlService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Object> create(@RequestBody CreateShortUrl input) {
        log.debug("Attempting to create new short url for [{}]", input.getDestination());

        try {
            ShortUrl shortUrl = shortUrlService.create(new URI(input.getDestination()));

            log.debug("Created short url for [{}] with code [{}]", shortUrl.getDestination(), shortUrl.getCode());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ShortUrlResponse.fromDomain(shortUrl));
        } catch (URISyntaxException exception) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<Object> find(@PathVariable String id) {
        log.debug("Attempting to find short url with id: [{}]", id);

        try {
            ShortUrl shortUrl = shortUrlRepository.getById(UUID.fromString(id));

            log.debug("Found short url with id: [{}]", id);

            return ResponseEntity.ok(ShortUrlResponse.fromDomain(shortUrl));
        } catch (EntityNotFoundException | IllegalArgumentException exception) {
            log.error("Error attemtping to find short url with id: [{}]", id, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Not found\"}");
        }
    }
}
