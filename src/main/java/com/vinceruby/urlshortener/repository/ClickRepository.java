package com.vinceruby.urlshortener.repository;

import com.vinceruby.urlshortener.domain.Click;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClickRepository extends JpaRepository<Click, UUID> {

    long countByShortUrlId(UUID id);

    List<Click> findAllByShortUrlId(UUID id);

    Page<Click> findByShortUrlId(UUID id, Pageable pageable);
}
