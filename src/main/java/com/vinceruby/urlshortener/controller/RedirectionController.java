package com.vinceruby.urlshortener.controller;

import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RedirectionController {

    private final ShortUrlRepository shortUrlRepository;

    @GetMapping("/{code}")
    public @ResponseBody ResponseEntity<Object> redirect(@PathVariable String code) {
        log.debug("Received request for code: [{}] attempting to find destination...", code);

        ShortUrl shortUrl = shortUrlRepository.findByCode(code);
        if (null == shortUrl) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Invalid URL\"}");
        }

        log.debug("Code: [{}] exists, redirecting to [{}]", code, shortUrl.getDestination());

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", shortUrl.getDestination().toString())
                .build();
    }
}
