package com.vinceruby.urlshortener.controller;

import com.vinceruby.urlshortener.domain.Click;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ClickRepository;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

@RequestMapping("/clicks")
@RequiredArgsConstructor
@RestController
@Slf4j
@Transactional
public class ClickController {

    @AllArgsConstructor
    @Builder
    @Getter
    @NoArgsConstructor
    @Setter
    public static class ClickResponse {

        private String id;
        private String clickedAt;
        private String shortCodeClicked;

        public static ClickResponse fromDomain(Click click) {
            return ClickResponse.builder()
                    .id(click.getId().toString())
                    .clickedAt(click.getCreatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    .shortCodeClicked(click.getShortUrl().getCode())
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    @Setter
    public static class ClickResponseList {

        private int count;
        private int page;
        private long total;
        private Collection<ClickResponse> clicks = new ArrayList<>();
    }

    private final ClickRepository clickRepository;
    private final ShortUrlRepository shortUrlRepository;

    @GetMapping("/{code}")
    public ResponseEntity<Object> indexForCode(
            @PathVariable String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.debug("Attempting to get clicks for code: [{}]", code);

        ShortUrl shortUrl = shortUrlRepository.findByCode(code);
        if (null == shortUrl) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Invalid code\"}");
        }

        if (page < 0) {
            page = 0;
        }

        if (size < 0) {
            size = 20;
        }

        Page<Click> pagedClicks = clickRepository.findByShortUrlId(shortUrl.getId(), PageRequest.of(page, size));

        ClickResponseList list = new ClickResponseList();
        list.setClicks(pagedClicks.stream().map(ClickResponse::fromDomain).toList())
                .setCount(pagedClicks.getNumberOfElements())
                .setPage(pagedClicks.getPageable().getPageNumber())
                .setTotal(pagedClicks.getTotalElements());

        return ResponseEntity.ok(list);
    }
}
