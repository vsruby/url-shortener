package com.vinceruby.urlshortener.service;

import com.vinceruby.urlshortener.contract.service.CodeGeneratorService;
import com.vinceruby.urlshortener.domain.ShortUrl;
import com.vinceruby.urlshortener.repository.ShortUrlRepository;
import com.vinceruby.urlshortener.contract.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private static final int TRIES_COUNT_THRESHOLD = 3;

    private final CodeGeneratorService codeGeneratorService;
    private final ShortUrlRepository shortUrlRepository;

    @Override
    public ShortUrl create(URI destination) {
        ShortUrl shortUrl = ShortUrl.builder()
                .code(generateUniqueCode())
                .destination(destination)
                .build();

        return shortUrlRepository.save(shortUrl);
    }

    private String generateUniqueCode() {
        String possibleCode = codeGeneratorService.generate();
        int tries = 0;
        while(shortUrlRepository.existsByCode(possibleCode)) {
            tries++;
            // if 3 tries still doesn't produce a unique code then try going with a larger length
            if (tries > TRIES_COUNT_THRESHOLD) {
                int modifier = tries / TRIES_COUNT_THRESHOLD;
                possibleCode = codeGeneratorService.generate(CodeGeneratorService.DEFAULT_CODE_LENGTH + modifier);
            } else {
                possibleCode = codeGeneratorService.generate();
            }
        }

        return possibleCode;
    }
}
