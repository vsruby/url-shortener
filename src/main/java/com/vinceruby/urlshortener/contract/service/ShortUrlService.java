package com.vinceruby.urlshortener.contract.service;

import com.vinceruby.urlshortener.domain.ShortUrl;

import java.net.URI;

public interface ShortUrlService {

    default ShortUrl create(String destination) {
        return create(URI.create(destination));
    }

    ShortUrl create(URI destination);
}
