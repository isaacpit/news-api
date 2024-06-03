package com.isaacpit.news.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseDto extends ResponseDtoAbstract {
    String status;
    Long totalResults;
    List<Article> articles;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Article {
        private String title;
        private String author;
        private String description;
        private String content;
        private String urlToImage;
        private String publishedAt;
        private Source source;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Source {
        private String name;
        private String id;
    }
}
