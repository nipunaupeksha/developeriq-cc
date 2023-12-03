package com.acmecorp.developeriq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "repository")
@Data
@Validated
public class Repository {
    @Id
    @JsonProperty("id")
    @Column(name = "repository_id", nullable = false)
    private Long id;

    @Embedded
    @JsonProperty("owner")
    private Owner owner;

    @JsonProperty("name")
    @Column(name = "repository_name", nullable = false)
    private String name;

    @Embeddable
    @Data
    public static class Owner {
        @Column(name = "owner_name")
        @JsonProperty("login")
        private String owner_name;
    }
}
