package com.isaacpit.news.api.dto;


import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GnewsResponseDto extends ResponseDtoAbstract {

    Long totalArticles;
    List<Article> articles;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Article {
        private String title;
        private String description;
        private String content;
        private String url;
        private String image;
        private String publishedAt;
        private Source source;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Source {
        private String name;
        private String url;
    }
}
