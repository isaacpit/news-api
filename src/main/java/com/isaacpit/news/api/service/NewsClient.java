package com.isaacpit.news.api.service;

import com.isaacpit.news.api.domain.SearchRequestParams;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequestParams;

public interface NewsClient {

    NewsResponse callSearchApi(SearchRequestParams searchRequestParams);

    NewsResponse callTopHeadlinesApi(TopHeadlinesRequestParams requestParams);
}
