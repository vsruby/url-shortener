package com.vinceruby.urlshortener.testUtils;

import com.github.javafaker.Faker;
import com.vinceruby.urlshortener.domain.ShortUrl;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

@UtilityClass
public class ShortUrlFactory {

    public static final Faker FAKER = new Faker();

    public static ShortUrl shortUrl() {
        return ShortUrl.builder()
                .id(UUID.randomUUID())
                .code(FAKER.lorem().characters(6))
                .destination(URI.create(FAKER.internet().url()))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}
