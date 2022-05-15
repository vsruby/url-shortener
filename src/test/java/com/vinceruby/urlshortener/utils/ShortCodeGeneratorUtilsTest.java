package com.vinceruby.urlshortener.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortCodeGeneratorUtilsTest {

    @Test
    public void itShouldGenerateARandomStringWithDefaultSize() {
        String random = ShortCodeGeneratorUtils.generate();

        assertEquals(ShortCodeGeneratorUtils.DEFAULT_STRING_LENGTH, random.length());
        assertTrue(StringUtils.isAlphanumeric(random));
    }

    @Test
    public void itShouldGenerateARandomStringWithDesiredSize() {
        int desiredSize = 10;
        String random = ShortCodeGeneratorUtils.generate(desiredSize);

        assertEquals(desiredSize, random.length());
        assertTrue(StringUtils.isAlphanumeric(random));
    }
}
