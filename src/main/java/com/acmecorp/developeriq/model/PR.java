package com.acmecorp.developeriq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pr")
@Data
@Validated
public class PR {
    @Id
    @Column(name = "user_name")
    private String user;

    @Column(name = "total_pr")
    @JsonProperty("total_count")
    private int totalIssues;
}
