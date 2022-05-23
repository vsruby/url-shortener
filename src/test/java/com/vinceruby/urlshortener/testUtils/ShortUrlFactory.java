package com.vinceruby.urlshortener.testUtils;

import com.vinceruby.urlshortener.domain.ShortUrl;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

import static com.vinceruby.urlshortener.testUtils.FakerProvider.INSTANCE;

@UtilityClass
public class ShortUrlFactory {

    public static ShortUrl make() {
        return ShortUrl.builder()
                .id(UUID.randomUUID())
                .code(INSTANCE.lorem().characters(6))
                .destination(URI.create(INSTANCE.internet().url()))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}
