package com.acmecorp.developeriq.exception.handler;

import com.acmecorp.developeriq.exception.InvalidRequestException;
import com.acmecorp.developeriq.exception.NotFoundException;
import com.acmecorp.developeriq.exception.TechnicalErrorException;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
@Getter
@ToString
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().is4xxClientError() || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

        if (httpResponse.getStatusCode().is5xxServerError()) {
            throw new TechnicalErrorException(httpResponse.getStatusText(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(httpResponse.getStatusText(), String.valueOf(HttpStatus.NOT_FOUND.value()));
            }
            throw new InvalidRequestException(String.valueOf(HttpStatus.BAD_REQUEST.value()), httpResponse.getStatusText());
        }
    }
}
