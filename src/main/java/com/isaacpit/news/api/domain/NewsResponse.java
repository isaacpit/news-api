package com.isaacpit.news.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.isaacpit.news.api.domain.enums.ApiClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The response class currently is shared among a few news clients since there is a lot of overlap.
 * If the fields were more mutually exclusive would probably have a specific field for each news client response.
 * Could also use specific field per client to support multi-client queries for aggregated results
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class NewsResponse {

    String status; // News API only
    Long totalArticles; // Gnews API=totalArticles, News API=totalResults
    Integer numArticlesReturned; // generated metadata
    String client; // generated metadata
    List<Article> articles;
    Error error;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Article {
        private String title;
        private String author; // News API only
        private String description;
        private String content;
        private String url; // Gnews API only
        private String image; // Gnews API=image, News API=urlToImage
        private String publishedAt;
        private Source source;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Source {
        private String name;
        private String url; // GNews API only
        private String id; // News API only

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        String errorMessage;
        Integer errorStatusCode;
    }
}
