package com.vinceruby.urlshortener.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "short_urls")
public class Click {

    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Id
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    @CreationTimestamp
    private OffsetDateTime createdAt;

    // ---- RELATIONSHIPS ---- //

    @Column(name = "short_url_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ShortUrl shortUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Click click = (Click) o;
        return Objects.equals(id, click.id);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

}
