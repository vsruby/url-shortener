package com.vinceruby.urlshortener.domain.converter;

import javax.persistence.AttributeConverter;
import java.net.URI;

public class UriAttributeConverter implements AttributeConverter<URI, String> {

    @Override
    public String convertToDatabaseColumn(URI value) {
        return value.toString();
    }

    @Override
    public URI convertToEntityAttribute(String value) {
        return URI.create(value);
    }
}
