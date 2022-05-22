package com.vinceruby.urlshortener.service;

import com.vinceruby.urlshortener.contract.service.CodeGeneratorService;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    public static final int MAX_CODE_LENGTH = 16;

    @Override
    public String generate() {
        return generate(DEFAULT_CODE_LENGTH);
    }

    @Override
    public String generate(int size) {
        int possibleSize = size;

        if (possibleSize < 1) {
            possibleSize = DEFAULT_CODE_LENGTH;
        } else if (possibleSize > MAX_CODE_LENGTH) {
            possibleSize = MAX_CODE_LENGTH;
        }

        return randomAlphanumeric(possibleSize);
    }
}
