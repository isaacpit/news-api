package com.isaacpit.news.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaacpit.news.api.domain.SearchRequest;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequest;
import com.isaacpit.news.api.domain.enums.ApiClient;
import com.isaacpit.news.api.mappers.ResponseDtoMapper;
import com.isaacpit.news.api.repository.NewsRepository;
import com.isaacpit.news.api.service.NewsClient;
import com.isaacpit.news.api.service.NewsClientProvider;
import com.isaacpit.news.api.service.NewsService;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@Builder
@Service
public class NewsServiceWithCaching implements NewsService {

    private NewsClientProvider newsClientProvider;
    private NewsRepository newsRepository;

    private final ObjectMapper objectMapper;
    private final ResponseDtoMapper mapper = Mappers.getMapper(ResponseDtoMapper.class);

    /**
     * checks cache first prior to making call. handles multiple news clients such as News API, GNews API, and a mocked local file news client
     */
    @Override
    public NewsResponse callSearchApiWithCaching(@NotNull SearchRequest searchRequest, ApiClient apiClient) {
        // check cache first
        String cacheKey = getSearchCacheKey(searchRequest, apiClient);
        Optional<NewsResponse> optionalCachedResponse = newsRepository.getCachedResponse(cacheKey);

        if (optionalCachedResponse.isPresent()) {
            log.info("cache-key={} found in cache", cacheKey);
            return optionalCachedResponse.get();
        }
        log.info("cache-key={} not found in cache", cacheKey);

        // selects specified news client provider
        NewsClient newsClient = newsClientProvider.getNewsClient(apiClient);
        // can add optional per-client validation on the search params here, possibly as a new NewsClient interface method like NewsClient.validateSearchParams()

        // fetch from API via news client facade
        NewsResponse response = newsClient.callSearchApi(searchRequest);
        enhanceResponseWithMetadata(response, apiClient);

        // update cache
        newsRepository.put(cacheKey, response);

        return response;
    }

    @Override
    public NewsResponse callTopHeadlinesWithCaching(@NotNull TopHeadlinesRequest requestParams, @NotNull ApiClient apiClient) {
        String cacheKey = getTopHeadlinesCacheKey(requestParams, apiClient);
        Optional<NewsResponse> optionalCachedResponse = newsRepository.getCachedResponse(cacheKey);

        if (optionalCachedResponse.isPresent()) {
            log.info("cache-key={} found in cache", cacheKey);
            return optionalCachedResponse.get();
        }
        log.info("cache-key={} not found in cache", cacheKey);

        // selects specified news client provider

        NewsClient newsClient = newsClientProvider.getNewsClient(apiClient);
        // can add optional per-client validation on the search params here, possibly as a new NewsClient interface method like NewsClient.validateSearchParams()

        // fetch from API via news client facade
        NewsResponse response = newsClient.callTopHeadlinesApi(requestParams);
        enhanceResponseWithMetadata(response, apiClient);

        // update cache
        newsRepository.put(cacheKey, response);

        return response;
    }

    private static void enhanceResponseWithMetadata(NewsResponse response, @NotNull ApiClient apiClient) {
        Optional.ofNullable(response.getArticles()).ifPresent(articles -> response.setNumArticlesReturned(articles.size()));
        response.setClient(apiClient.getValue().toLowerCase());
    }

    /**
     * using client + searchParams json representation as key/value pairs to represent the cache key. probably better to move to hashing instead for the search params the SearchParam.query property can be large
     */
    @SneakyThrows({JsonProcessingException.class})
    private String getSearchCacheKey(SearchRequest searchRequest, ApiClient apiClient) {
        return MessageFormat.format("client={0},params={1}", apiClient.getValue(), objectMapper.writeValueAsString(searchRequest));
    }

    @SneakyThrows({JsonProcessingException.class})
    private String getTopHeadlinesCacheKey(TopHeadlinesRequest requestParams, ApiClient apiClient) {
        return MessageFormat.format("client={0},params={1}", apiClient.getValue(), objectMapper.writeValueAsString(requestParams));
    }
}
