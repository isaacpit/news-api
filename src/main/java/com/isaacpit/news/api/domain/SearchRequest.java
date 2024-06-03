package com.isaacpit.news.api.domain;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@Builder
public class SearchRequest {
    @Nullable
    String query;
    @Nullable
    String author;
    @Nullable
    String searchIn;
    @Nullable
    Integer pageSize;
    @Nullable
    Integer page;
}
