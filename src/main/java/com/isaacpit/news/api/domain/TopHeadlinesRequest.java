package com.isaacpit.news.api.domain;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@Builder
public class TopHeadlinesRequest {
    @Nullable
    Integer pageSize;
    @Nullable
    Integer pageNumber;
    @Nullable
    String query;
}

