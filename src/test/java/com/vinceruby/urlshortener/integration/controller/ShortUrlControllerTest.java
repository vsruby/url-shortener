package com.vinceruby.urlshortener.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceruby.urlshortener.controller.ShortUrlController.ShortUrlResponse;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        refresh = AFTER_EACH_TEST_METHOD
)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class ShortUrlControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @BeforeEach
    public void setup() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void itShouldCreateAShortUrl() throws Exception {
        String uri = "https://google.com";

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var result = restTemplate.exchange(
                "http://localhost:" + port + "/short-urls",
                HttpMethod.POST,
                new HttpEntity<>("{\"destination\": \"" + uri + "\"}", headers),
                Object.class
        );

        ShortUrlResponse response = MAPPER.convertValue(result.getBody(), ShortUrlResponse.class);
        ShortUrl shortUrl = shortUrlRepository.getById(UUID.fromString(response.getId()));

        assertEquals(uri, response.getDestination());

        assertEquals(response.getId(), shortUrl.getId().toString());
        assertEquals(response.getCode(), shortUrl.getCode());
        assertEquals(response.getDestination(), shortUrl.getDestination().toString());
    }
}