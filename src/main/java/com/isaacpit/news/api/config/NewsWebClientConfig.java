package com.isaacpit.news.api.config;

import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;


@Slf4j
@EqualsAndHashCode
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "news-client-configs")
public class NewsWebClientConfig {

    private Map<String, String> commonHeaders;
    private NewsWebClientConfigProps gnews;
    private NewsWebClientConfigProps news;

    @PostConstruct
    public void init() {
        log.info("webClientConfigs: {}", this);
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsWebClientConfigProps {
        private String protocol;
        private String host;
        private String apikey;
        private Duration requestTimeoutDuration;
        private ParamNames paramNames;
        private ApiConfig searchApi;
        private ApiConfig topHeadlinesApi;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiConfig {
        private String apiPath;
        private Map<String, String> queryParams;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParamNames {
        private String pageNumberName;
        private String pageSizeName;
        private String searchInName;
    }
}
