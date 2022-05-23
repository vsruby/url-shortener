package com.vinceruby.urlshortener.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ClickRepository;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.testUtils.ShortUrlFactory;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Duration;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        refresh = AFTER_EACH_TEST_METHOD
)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RedirectionControllerTest {

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
        // disable redirecting
        SimpleClientHttpRequestFactory clientFactory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
            }
        };
        restTemplate = (new RestTemplateBuilder())
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .requestFactory(() -> clientFactory)
                .build();
    }

    @Test
    public void itShouldRedirectToTheCorrectLocation() {
        ShortUrl shortUrl = shortUrlRepository.save(ShortUrlFactory.make());

        var result = restTemplate.exchange(
                "http://localhost:" + port + "/" + shortUrl.getCode(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Object.class
        );

        assertEquals(HttpStatus.MOVED_PERMANENTLY, result.getStatusCode());
        assertEquals(shortUrl.getDestination(), result.getHeaders().getLocation());

        // confirm the click was logged
        assertEquals(1, clickRepository.countByShortUrlId(shortUrl.getId()));
    }
}
