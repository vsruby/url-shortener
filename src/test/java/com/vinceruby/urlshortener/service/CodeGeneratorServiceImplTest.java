package com.vinceruby.urlshortener.service;

import com.vinceruby.urlshortener.contract.service.CodeGeneratorService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeGeneratorServiceImplTest {

    private CodeGeneratorServiceImpl service;

    @BeforeEach
    public void setup() {
        service = new CodeGeneratorServiceImpl();
    }

    @Test
    public void itShouldGenerateACodeBasedOnTheDefaultLength() {
        String code = service.generate();

        assertEquals(CodeGeneratorService.DEFAULT_CODE_LENGTH, code.length());
        assertTrue(StringUtils.isAlphanumeric(code));
    }

    @Test
    public void itShouldGenerateACodeWithSpecifiedLength() {
        int size = CodeGeneratorService.DEFAULT_CODE_LENGTH + 1;
        String code = service.generate(size);

        assertEquals(size, code.length());
        assertTrue(StringUtils.isAlphanumeric(code));
    }

    @Test
    public void itShouldGenerateCodeWithDefaultLengthIfSizeIsLessThanOne() {
        String code = service.generate(0);

        assertEquals(CodeGeneratorService.DEFAULT_CODE_LENGTH, code.length());
        assertTrue(StringUtils.isAlphanumeric(code));
    }

    @Test
    public void itShouldGenerateCodeWithMaxLengthIfSizeProvidedIsGreaterThanTheMax() {
        String code = service.generate(CodeGeneratorServiceImpl.MAX_CODE_LENGTH + 1);

        assertEquals(CodeGeneratorServiceImpl.MAX_CODE_LENGTH, code.length());
        assertTrue(StringUtils.isAlphanumeric(code));
    }
}
