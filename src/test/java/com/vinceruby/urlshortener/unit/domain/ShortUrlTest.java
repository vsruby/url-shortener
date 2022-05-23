package com.vinceruby.urlshortener.unit.domain;

import com.vinceruby.urlshortener.domain.ShortUrl;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ShortUrlTest {

    @Test
    public void itShouldReturnTrueWhenIdsMatch() {
        UUID id = UUID.randomUUID();

        ShortUrl shortUrl1 = (new ShortUrl()).setId(id);
        ShortUrl shortUrl2 = (new ShortUrl()).setId(id);

        assertEquals(shortUrl1, shortUrl2);
    }

    @Test
    public void itShouldReturnFalseWhenIdsDoNotMatch() {
        ShortUrl shortUrl1 = (new ShortUrl()).setId(UUID.randomUUID());
        ShortUrl shortUrl2 = (new ShortUrl()).setId(UUID.randomUUID());

        assertNotEquals(shortUrl1, shortUrl2);
    }
}
