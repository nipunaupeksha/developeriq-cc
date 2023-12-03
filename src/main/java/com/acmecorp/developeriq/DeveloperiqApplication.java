package com.acmecorp.developeriq;

import com.acmecorp.developeriq.exception.handler.RestTemplateResponseErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackages = "com.acmecorp.developeriq")
@EnableJpaRepositories(basePackages = "com.acmecorp.developeriq")
@ComponentScan(basePackages = "com.acmecorp.developeriq")
public class DeveloperiqApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeveloperiqApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getDefault());
    }

}
