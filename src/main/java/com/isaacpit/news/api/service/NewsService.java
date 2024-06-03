package com.isaacpit.news.api.service;

import com.isaacpit.news.api.domain.SearchRequestParams;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequestParams;
import com.isaacpit.news.api.domain.enums.ApiClient;

public interface NewsService {

    NewsResponse callSearchApiWithCaching(SearchRequestParams searchRequestParams, ApiClient apiClient);

    NewsResponse callTopHeadlinesWithCaching(TopHeadlinesRequestParams requestParams, ApiClient apiClient);
}
