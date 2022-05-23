package com.vinceruby.urlshortener.unit.controller;

import com.vinceruby.urlshortener.contract.service.ShortUrlService;
import com.vinceruby.urlshortener.controller.ShortUrlController;
import com.vinceruby.urlshortener.controller.ShortUrlController.CreateShortUrl;
import com.vinceruby.urlshortener.controller.ShortUrlController.ShortUrlResponse;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.testUtils.ShortUrlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortUrlControllerTest {

    @Nested
    public class ShortUrlResponseTest {

        @Test
        public void itShouldBuildDTOFromDomainClass() {
            ShortUrl shortUrl = ShortUrlFactory.shortUrl();

            ShortUrlResponse response = ShortUrlResponse.fromDomain(shortUrl);

            assertEquals(shortUrl.getId().toString(), response.getId());
            assertEquals(shortUrl.getCode(), response.getCode());
            assertEquals(shortUrl.getDestination().toString(), response.getDestination());
            assertEquals(shortUrl.getCreatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    response.getCreatedAt());
            assertEquals(shortUrl.getUpdatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    response.getUpdatedAt());
        }
    }

    private ShortUrlRepository mockShortUrlRepository;
    private ShortUrlService mockShortUrlService;

    private ShortUrlController controller;

    @BeforeEach
    public void setup() {
        mockShortUrlRepository = Mockito.mock(ShortUrlRepository.class);
        mockShortUrlService = Mockito.mock(ShortUrlService.class);

        controller = new ShortUrlController(mockShortUrlRepository, mockShortUrlService);
    }

    @Test
    public void itShouldCreateTheShortUrlWithTheProvidedDestination() {
        ShortUrl shortUrl = ShortUrlFactory.shortUrl();
        Mockito.when(mockShortUrlService.create(Mockito.eq(shortUrl.getDestination()))).thenReturn(shortUrl);

        var response = controller.create(CreateShortUrl.builder()
                .destination(shortUrl.getDestination().toString()).build());

        var body = (ShortUrlResponse) response.getBody();

        assertEquals(shortUrl.getId().toString(), body.getId());
        assertEquals(shortUrl.getCode(), body.getCode());
        assertEquals(shortUrl.getDestination().toString(), body.getDestination());
        assertEquals(shortUrl.getCreatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                body.getCreatedAt());
        assertEquals(shortUrl.getUpdatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                body.getUpdatedAt());
    }
}
