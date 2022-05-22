package com.vinceruby.urlshortener.repository;

import com.vinceruby.urlshortener.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, UUID> {

    boolean existsByCode(String code);

    ShortUrl findByCode(String code);
}
