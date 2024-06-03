package com.isaacpit.news.api.config;


import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;


import java.util.function.Consumer;

@Slf4j
@Configuration
@Builder
public class NewsRestTemplate {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
