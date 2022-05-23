package com.vinceruby.urlshortener.contract.service;

import com.vinceruby.urlshortener.domain.Click;
import com.vinceruby.urlshortener.domain.ShortUrl;

public interface ClickService {

    Click create(ShortUrl shortUrl);
}
