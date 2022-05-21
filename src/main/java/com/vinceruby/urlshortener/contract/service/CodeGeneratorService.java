package com.vinceruby.urlshortener.contract.service;

public interface CodeGeneratorService {

    int DEFAULT_CODE_LENGTH = 5;

    /**
     * Generate random alphanumeric string of default size defined by {@link #DEFAULT_CODE_LENGTH}.
     *
     * @return Random alphanumeric string.
     */
    String generate();

    /**
     * Generate random alphanumeric string based on provided size.
     *
     * @param size The desired size of the random string.
     *
     * @return Random alphanumeric string.
     */
    String generate(int size);
}
