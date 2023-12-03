package com.acmecorp.developeriq.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssueDTO {
    private String developerName;
    private int totalIssueCount;
}
