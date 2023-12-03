package com.acmecorp.developeriq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name = "commit")
@Data
@Validated
public class Commit {
    @Id
    @Column(name = "commit_id")
    @JsonProperty("sha")
    private String sha;

    @Embedded
    @JsonProperty("committer")
    private Committer committer;

    @Column(name = "repository_name")
    private String repositoryName;

    @Embeddable
    @Data
    public static class Committer {
        @Column(name = "committer_name")
        @JsonProperty("login")
        private String name;
    }
}
