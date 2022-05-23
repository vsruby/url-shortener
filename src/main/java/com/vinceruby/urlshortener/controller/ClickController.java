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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Getter
    @NoArgsConstructor
    public static class ClickResponseList {

        private Collection<ClickResponse> clicks = new ArrayList<>();
        private int total;

        public ClickResponseList(Collection<ClickResponse> clicks) {
            this.clicks = clicks;
            this.total = clicks.size();
        }

        public ClickResponseList setClicks(Collection<ClickResponse> clicks) {
            this.clicks = clicks;
            this.total = clicks.size();

            return this;
        }

        public ClickResponseList setTotal(int total) {
            if (total == this.clicks.size()) {
                this.total = total;
            } else {
                this.total = this.clicks.size();
            }

            return this;
        }
    }

    private final ClickRepository clickRepository;
    private final ShortUrlRepository shortUrlRepository;

    @GetMapping("/{code}")
    @Transactional
    public ResponseEntity<Object> indexForCode(@PathVariable String code) {
        log.debug("Attempting to get clicks for code: [{}]", code);

        ShortUrl shortUrl = shortUrlRepository.findByCode(code);
        if (null == shortUrl) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Invalid code\"}");
        }

        List<Click> clicks = clickRepository.findAllByShortUrlId(shortUrl.getId());

        ClickResponseList list = new ClickResponseList();
        list.setClicks(clicks.stream().map(ClickResponse::fromDomain).toList())
                .setTotal(clicks.size());

        return ResponseEntity.ok(list);
    }
}
