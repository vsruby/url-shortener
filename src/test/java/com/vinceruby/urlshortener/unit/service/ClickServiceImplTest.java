package com.vinceruby.urlshortener.unit.service;

import com.vinceruby.urlshortener.domain.Click;
import com.vinceruby.urlshortener.repository.ClickRepository;
import com.vinceruby.urlshortener.service.ClickServiceImpl;
import com.vinceruby.urlshortener.testUtils.ClickFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClickServiceImplTest {

    private ClickRepository mockClickRepository;

    private ClickServiceImpl service;

    @BeforeEach
    public void setup() {
        mockClickRepository = Mockito.mock(ClickRepository.class);

        service = new ClickServiceImpl(mockClickRepository);
    }

    @Test
    public void itShouldCreateClickForAShortUrl() {
        Click expectedClick = ClickFactory.make();

        Mockito.when(mockClickRepository.save(Mockito.any())).thenReturn(expectedClick);

        Click click = service.create(expectedClick.getShortUrl());

        assertEquals(expectedClick, click);
    }
}
