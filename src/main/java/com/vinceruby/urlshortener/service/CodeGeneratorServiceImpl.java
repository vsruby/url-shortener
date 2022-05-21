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
        return randomAlphanumeric(Math.max(size, MAX_CODE_LENGTH));
    }
}
