package com.isaacpit.news.api.service;

import com.isaacpit.news.api.domain.SearchRequest;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequest;

public interface NewsClient {

    NewsResponse callSearchApi(SearchRequest searchRequest);

    NewsResponse callTopHeadlinesApi(TopHeadlinesRequest requestParams);
}
