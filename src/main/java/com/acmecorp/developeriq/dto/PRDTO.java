package com.acmecorp.developeriq.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PRDTO {
    private String developerName;
    private int totalPRCount;
}
