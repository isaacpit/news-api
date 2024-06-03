package com.isaacpit.news.api.repository.impl;

import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class InMemoryNewsRepository implements NewsRepository {

    ConcurrentHashMap<String, NewsResponse> cache = new ConcurrentHashMap<>();

    @Override
    public Optional<NewsResponse> getCachedResponse(String cacheKey) {

        Optional<NewsResponse> cachedResponse = Optional.ofNullable(cache.get(cacheKey));
        return cachedResponse;
    }

    @Override
    public void put(String key, NewsResponse response) {
        cache.put(key, response);
    }

}
