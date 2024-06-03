package com.isaacpit.news.api.service;

import com.isaacpit.news.api.domain.enums.ApiClient;
import com.isaacpit.news.api.service.client.GnewsApiClient;
import com.isaacpit.news.api.service.client.MockApiClient;
import com.isaacpit.news.api.service.client.NewsApiClient;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Class to switch between external news clients easily
 */
@Slf4j
@Builder
@Component
public class NewsClientProvider {

    private GnewsApiClient gnewsApiClient;
    private NewsApiClient newsApiClient;
    private MockApiClient mockApiClient;

    public NewsClient getNewsClient(ApiClient apiClient) {
        if (ApiClient.GNEWS.equals(apiClient)) {
            return gnewsApiClient;
        } else if (ApiClient.NEWS.equals(apiClient)) {
            return newsApiClient;
        }
        return mockApiClient;
    }

}
