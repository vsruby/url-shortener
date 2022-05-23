package com.vinceruby.urlshortener.integration.service;

import com.vinceruby.urlshortener.contract.service.CodeGeneratorService;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.service.ShortUrlServiceImpl;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        refresh = AFTER_EACH_TEST_METHOD
)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
public class ShortUrlServiceImplTest {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    private ShortUrlServiceImpl shortUrlService;

    @BeforeEach
    public void setup() {
        shortUrlService = new ShortUrlServiceImpl(codeGeneratorService, shortUrlRepository);
    }

    @Test
    public void itShouldCreateShortUrl() {
        String uri = "https://google.com";

        ShortUrl shortUrl = shortUrlService.create(uri);

        assertTrue(shortUrlRepository.existsById(shortUrl.getId()));
    }
}
