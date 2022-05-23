package com.vinceruby.urlshortener.unit.domain;

import com.vinceruby.urlshortener.domain.Click;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ClickTest {

    @Test
    public void itShouldReturnTrueWhenIdsMatch() {
        UUID id = UUID.randomUUID();

        Click click1 = (new Click()).setId(id);
        Click click2 = (new Click()).setId(id);

        assertEquals(click1, click2);
    }

    @Test
    public void itShouldReturnFalseWhenIdsDoNotMatch() {
        Click click1 = (new Click()).setId(UUID.randomUUID());
        Click click2 = (new Click()).setId(UUID.randomUUID());

        assertNotEquals(click1, click2);
    }
}
