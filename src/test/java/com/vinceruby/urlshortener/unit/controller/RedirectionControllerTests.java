package com.vinceruby.urlshortener.unit.controller;

import com.vinceruby.urlshortener.contract.service.ClickService;
import com.vinceruby.urlshortener.controller.RedirectionController;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.testUtils.ShortUrlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RedirectionControllerTests {

    private ClickService mockClickService;
    private ShortUrlRepository mockShortUrlRepository;

    private RedirectionController controller;

    @BeforeEach
    public void setup() {
        mockClickService = Mockito.mock(ClickService.class);
        mockShortUrlRepository = Mockito.mock(ShortUrlRepository.class);

        controller = new RedirectionController(mockClickService, mockShortUrlRepository);
    }

    @Test
    public void itShouldReturnResponseWithTheCorrectLocationHeader() {
        ShortUrl shortUrl = ShortUrlFactory.make();
        Mockito.when(mockShortUrlRepository.findByCode(shortUrl.getCode())).thenReturn(shortUrl);

        var response = controller.redirect(shortUrl.getCode());

        assertEquals(shortUrl.getDestination().toString(), response.getHeaders().get("Location").get(0));
        assertEquals(HttpStatus.MOVED_PERMANENTLY, response.getStatusCode());
        Mockito.verify(mockClickService).create(Mockito.eq(shortUrl));
    }

    @Test
    public void itShouldReturnErrorResponseWhenInvalidCodeIsProvided() {
        String invalidCode = "er404";
        Mockito.when(mockShortUrlRepository.findByCode(invalidCode)).thenReturn(null);

        var response = controller.redirect(invalidCode);

        assertNull(response.getHeaders().get("Location"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Mockito.verifyNoInteractions(mockClickService);
    }
}
