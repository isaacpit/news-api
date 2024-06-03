package com.isaacpit.news.api.service;

import com.isaacpit.news.api.domain.SearchRequest;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequest;
import com.isaacpit.news.api.domain.enums.ApiClient;

public interface NewsService {

    NewsResponse callSearchApiWithCaching(SearchRequest searchRequest, ApiClient apiClient);

    NewsResponse callTopHeadlinesWithCaching(TopHeadlinesRequest requestParams, ApiClient apiClient);
}
