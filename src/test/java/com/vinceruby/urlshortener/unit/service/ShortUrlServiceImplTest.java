package com.vinceruby.urlshortener.unit.service;

import com.vinceruby.urlshortener.contract.service.CodeGeneratorService;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.service.ShortUrlServiceImpl;
import com.vinceruby.urlshortener.testUtils.ShortUrlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URISyntaxException;

import static com.vinceruby.urlshortener.contract.service.CodeGeneratorService.DEFAULT_CODE_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortUrlServiceImplTest {

    private CodeGeneratorService mockCodeGeneratorService;
    private ShortUrlRepository mockShortUrlRepository;

    private ShortUrlServiceImpl service;

    @BeforeEach
    public void setup() {
        mockCodeGeneratorService = Mockito.mock(CodeGeneratorService.class);
        mockShortUrlRepository = Mockito.mock(ShortUrlRepository.class);

        service = new ShortUrlServiceImpl(mockCodeGeneratorService, mockShortUrlRepository);
    }

    @Test
    public void itShouldCreateANewShortUrl() throws URISyntaxException {
        String expectedCode = "aaaaa";
        URI expectedUri = new URI("https://google.com");
        ShortUrl expectedShortUrl = ShortUrlFactory.shortUrl().setCode(expectedCode).setDestination(expectedUri);

        Mockito.when(mockCodeGeneratorService.generate()).thenReturn(expectedCode);
        Mockito.when(mockShortUrlRepository.save(Mockito.argThat(arg -> arg.getCode().equals(expectedCode))))
                .thenReturn(expectedShortUrl);
        Mockito.when(mockShortUrlRepository.existsByCode(Mockito.eq(expectedCode))).thenReturn(false);

        ShortUrl actualShortUrl = service.create(expectedUri);

        assertEquals(expectedShortUrl, actualShortUrl);
    }

    @Test
    public void itShouldCreateANewShortUrlWithLargerLengthAfterTheTriesCount() throws URISyntaxException {
        String expectedCode = "123456";
        URI expectedUri = new URI("https://google.com");
        ShortUrl expectedShortUrl = ShortUrlFactory.shortUrl().setCode(expectedCode).setDestination(expectedUri);

        Mockito.when(mockCodeGeneratorService.generate()).thenReturn("12345");
        Mockito.when(mockCodeGeneratorService.generate(DEFAULT_CODE_LENGTH + 1))
                .thenReturn(expectedCode);
        Mockito.when(mockShortUrlRepository.save(Mockito.argThat(arg -> arg.getCode().equals(expectedCode))))
                .thenReturn(expectedShortUrl);
        // always return true when the length of the code is the default length
        Mockito.doReturn(true).when(mockShortUrlRepository)
                .existsByCode(Mockito.argThat(arg -> arg.length() == DEFAULT_CODE_LENGTH));
        // once the check for a length greater than the default happens then return false
        Mockito.doReturn(false).when(mockShortUrlRepository)
                .existsByCode(Mockito.argThat(arg -> arg.length() == DEFAULT_CODE_LENGTH + 1));

        ShortUrl actualShortUrl = service.create(expectedUri);

        assertEquals(expectedShortUrl, actualShortUrl);
    }
}
