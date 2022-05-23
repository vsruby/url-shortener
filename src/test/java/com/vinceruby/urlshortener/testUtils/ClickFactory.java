package com.vinceruby.urlshortener.testUtils;

import com.vinceruby.urlshortener.domain.Click;
import lombok.experimental.UtilityClass;

import java.time.OffsetDateTime;
import java.util.UUID;

@UtilityClass
public class ClickFactory {

    public static Click make() {
        return Click.builder()
                .id(UUID.randomUUID())
                .shortUrl(ShortUrlFactory.make())
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
