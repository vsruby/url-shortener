package com.vinceruby.urlshortener.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceruby.urlshortener.controller.ClickController.ClickResponseList;
import com.vinceruby.urlshortener.domain.Click;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ClickRepository;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.testUtils.ClickFactory;
import com.vinceruby.urlshortener.testUtils.ShortUrlFactory;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        refresh = AFTER_EACH_TEST_METHOD
)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClickControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @Autowired
    private ClickRepository clickRepository;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @BeforeEach
    public void setup() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void itShouldReturnAllClicksForAShortUrl() {
        ShortUrl shortUrl = shortUrlRepository.save(ShortUrlFactory.make());

        int expectedTotal = 5;
        IntStream.range(0, expectedTotal).forEach(i -> {
            Click click = ClickFactory.make().setShortUrl(shortUrl);
            clickRepository.save(click);
        });

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.exchange(
                "http://localhost:" + port + "/clicks/" + shortUrl.getCode(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        );

        ClickResponseList response = MAPPER.convertValue(result.getBody(), ClickResponseList.class);

        assertEquals(expectedTotal, response.getClicks().size());
        assertEquals(0, response.getPage());
        assertEquals(expectedTotal, response.getCount());
        assertEquals(expectedTotal, response.getTotal());
    }

    @Test
    public void itShouldReturnPagedClicksForAShortUrl() {
        ShortUrl shortUrl = shortUrlRepository.save(ShortUrlFactory.make());

        int expectedTotal = 5;
        IntStream.range(0, expectedTotal).forEach(i -> {
            Click click = ClickFactory.make().setShortUrl(shortUrl);
            clickRepository.save(click);
        });

        int expectedSize = 3;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.exchange(
                "http://localhost:" + port + "/clicks/" + shortUrl.getCode() + "?size=" + expectedSize,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        );

        ClickResponseList response = MAPPER.convertValue(result.getBody(), ClickResponseList.class);

        assertEquals(expectedSize, response.getClicks().size());
        assertEquals(0, response.getPage());
        assertEquals(expectedSize, response.getCount());
        assertEquals(expectedTotal, response.getTotal());
    }

    @Test
    public void itShouldOnlyReturnClicksForAShortUrl() {
        ShortUrl shortUrl = shortUrlRepository.save(ShortUrlFactory.make());
        ShortUrl shortUrlToBeIgnored = shortUrlRepository.save(ShortUrlFactory.make());

        int expectedTotal = 5;
        IntStream.range(0, expectedTotal).forEach(i -> {
            Click click = ClickFactory.make().setShortUrl(shortUrl);
            clickRepository.save(click);

            Click clickToBeIgnored = ClickFactory.make().setShortUrl(shortUrlToBeIgnored);
            clickRepository.save(clickToBeIgnored);
        });

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.exchange(
                "http://localhost:" + port + "/clicks/" + shortUrl.getCode(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        );

        ClickResponseList response = MAPPER.convertValue(result.getBody(), ClickResponseList.class);

        assertEquals(expectedTotal, response.getClicks().size());
        assertEquals(0, response.getPage());
        assertEquals(expectedTotal, response.getCount());
        assertEquals(expectedTotal, response.getTotal());
        assertFalse(response.getClicks().stream()
                .anyMatch(item -> item.getShortCodeClicked().equals(shortUrlToBeIgnored.getCode())));
    }
}
