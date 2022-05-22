package com.vinceruby.urlshortener.testUtils;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FakerProvider {

    public static final Faker INSTANCE = new Faker();
}
