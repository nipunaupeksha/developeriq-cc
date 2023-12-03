package com.acmecorp.developeriq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "issue")
@Data
@Validated
public class Issue {
    @Id
    @Column(name = "user_name")
    private String user;

    @Column(name = "total_issues")
    @JsonProperty("total_count")
    private int totalIssues;
}
