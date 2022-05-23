package com.vinceruby.urlshortener.repository;

import com.vinceruby.urlshortener.domain.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClickRepository extends JpaRepository<Click, UUID> {

    List<Click> findAllByShortUrlId(UUID id);
}
