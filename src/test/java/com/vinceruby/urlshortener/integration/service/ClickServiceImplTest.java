package com.vinceruby.urlshortener.integration.service;

import com.vinceruby.urlshortener.contract.service.ClickService;
import com.vinceruby.urlshortener.domain.Click;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ClickRepository;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.service.ClickServiceImpl;
import com.vinceruby.urlshortener.testUtils.ShortUrlFactory;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureEmbeddedDatabase(
        provider = ZONKY,
        refresh = AFTER_EACH_TEST_METHOD
)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
public class ClickServiceImplTest {

    @Autowired
    private ClickRepository clickRepository;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    private ClickServiceImpl clickServiceImpl;

    @BeforeEach
    public void setup() {
        clickServiceImpl = new ClickServiceImpl(clickRepository);
    }

    @Test
    public void itShouldCreateClickForShortUrl() {
        ShortUrl shortUrl = shortUrlRepository.save(ShortUrlFactory.make());

        Click click = clickServiceImpl.create(shortUrl);

        shortUrl = shortUrlRepository.getById(shortUrl.getId());

        assertTrue(clickRepository.existsById(click.getId()));
        assertEquals(shortUrl, click.getShortUrl());
        assertFalse(clickRepository.findAllByShortUrlId(shortUrl.getId()).isEmpty());
    }
}
