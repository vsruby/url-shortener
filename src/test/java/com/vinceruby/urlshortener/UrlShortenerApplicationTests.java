package com.vinceruby.urlshortener;

import com.vinceruby.urlshortener.controller.RedirectionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UrlShortenerApplicationTests {

	@Autowired
	private RedirectionController redirectionController;

	@Test
	void itShouldPassTheSmokeTest() {
		assertNotNull(redirectionController);
	}
}
