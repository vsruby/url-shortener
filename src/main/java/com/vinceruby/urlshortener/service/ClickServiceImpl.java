package com.vinceruby.urlshortener.service;

import com.vinceruby.urlshortener.contract.service.ClickService;
import com.vinceruby.urlshortener.domain.Click;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ClickRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClickServiceImpl implements ClickService {

    private final ClickRepository clickRepository;

    @Override
    public Click create(ShortUrl shortUrl) {
        log.debug("Logging new click for code [{}]", shortUrl.getCode());

        Click click = Click.builder()
                .shortUrl(shortUrl)
                .build();

        return clickRepository.save(click);
    }
}
