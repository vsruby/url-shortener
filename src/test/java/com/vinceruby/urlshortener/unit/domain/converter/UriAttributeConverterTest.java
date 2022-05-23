package com.vinceruby.urlshortener.unit.domain.converter;

import com.vinceruby.urlshortener.domain.converter.UriAttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UriAttributeConverterTest {

    private static final String URI_STRING = "https://www.google.com/search?q=hello+world";

    private UriAttributeConverter converter;

    @BeforeEach
    public void setup() {
        converter = new UriAttributeConverter();
    }

    @Test
    public void itShouldGenerateTheDatabaseValueForUri() throws URISyntaxException {
        URI uri = new URI(URI_STRING);

        assertEquals(URI_STRING, converter.convertToDatabaseColumn(uri));
    }

    @Test
    public void itShouldGenerateTheEntityValueForString() throws URISyntaxException {
        URI uri = new URI(URI_STRING);

        assertEquals(uri, converter.convertToEntityAttribute(URI_STRING));
    }
}
