package com.vinceruby.urlshortener.unit.controller;

import com.vinceruby.urlshortener.controller.ClickController;
import com.vinceruby.urlshortener.controller.ClickController.ClickResponseList;
import com.vinceruby.urlshortener.domain.Click;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ClickRepository;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.testUtils.ClickFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClickControllerTest {

    private ClickRepository mockClickRepository;
    private ShortUrlRepository mockShortUrlRepository;

    private ClickController controller;

    @BeforeEach
    public void setup() {
        mockClickRepository = Mockito.mock(ClickRepository.class);
        mockShortUrlRepository = Mockito.mock(ShortUrlRepository.class);

        controller = new ClickController(mockClickRepository, mockShortUrlRepository);
    }

    @Test
    public void itShouldReturnResponseWithResults() {
        Click click = ClickFactory.make();
        ShortUrl shortUrl = click.getShortUrl();

        int expectedCount = 1;
        int expectedPage = 0;
        int expectedTotal = 1;
        List<Click> expectedClicks = List.of(click);
        Page<Click> mockPage = new PageImpl<>(expectedClicks, PageRequest.of(expectedPage, expectedCount), expectedTotal);

        Mockito.when(mockShortUrlRepository.findByCode(shortUrl.getCode())).thenReturn(shortUrl);
        Mockito.when(mockClickRepository.findByShortUrlId(Mockito.eq(shortUrl.getId()), Mockito.any()))
                .thenReturn(mockPage);

        var response = controller.indexForCode(shortUrl.getCode(), expectedPage, expectedCount);

        var body = (ClickResponseList) response.getBody();

        assertEquals(expectedCount, body.getCount());
        assertEquals(expectedPage, body.getPage());
        assertEquals(expectedTotal, body.getTotal());
        assertTrue(body.getClicks().stream().anyMatch(item -> item.getId().equals(click.getId().toString())));
    }

    @Test
    public void itShouldReturnErrorResponseWhenInvalidCodeIsProvided() {
        String invalidCode = "er404";
        Mockito.when(mockShortUrlRepository.findByCode(invalidCode)).thenReturn(null);

        var response = controller.indexForCode(invalidCode, 0, 20);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Mockito.verifyNoInteractions(mockClickRepository);
    }
}
