package com.vinceruby.urlshortener.domain;

import com.vinceruby.urlshortener.domain.converter.UriAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "short_urls")
public class ShortUrl {

    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Id
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "destination")
    @Convert(converter = UriAttributeConverter.class)
    private URI destination;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    // ---- RELATIONSHIPS ---- //

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shortUrl", orphanRemoval = true)
    private Collection<Click> clicks = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShortUrl shortUrl = (ShortUrl) o;
        return Objects.equals(id, shortUrl.id);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
