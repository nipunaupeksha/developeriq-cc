package com.acmecorp.developeriq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class ModelApiResponse {
    @JsonProperty("code")
    private Integer code = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("message")
    private String message = null;

    public ModelApiResponse(Integer code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }
}
