package com.isaacpit.news.api.repository;

import com.isaacpit.news.api.domain.NewsResponse;

import java.util.Optional;

public interface NewsRepository {

    Optional<NewsResponse> getCachedResponse(String cacheKey);

    void put(String key, NewsResponse response);
}
