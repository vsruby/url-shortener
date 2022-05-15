package com.vinceruby.urlshortener.utils;

import lombok.experimental.UtilityClass;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@UtilityClass
public class ShortCodeGeneratorUtils {

    static final int DEFAULT_STRING_LENGTH = 5;

    /**
     * Generate random alphanumeric string of default size defined at {@link #DEFAULT_STRING_LENGTH}.
     *
     * @return Random alphanumeric string.
     */
    public static String generate() {
        return generate(DEFAULT_STRING_LENGTH);
    }

    /**
     * Generate random alphanumeric string based on provided size.
     *
     * @param size The desired size of the random string.
     *
     * @return Random alphanumeric string.
     */
    public static String generate(int size) {
        return randomAlphanumeric(size);
    }
}
