package com.acmecorp.developeriq.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommitDTO {
    private int id;
    private String author;
    private String repositoryName;
    private int numberOfCommits;
}
