package com.isaacpit.news.api.service.client;

import com.isaacpit.news.api.domain.SearchRequest;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequest;
import com.isaacpit.news.api.dto.NewsResponseDto;
import com.isaacpit.news.api.mappers.ResponseDtoMapper;
import com.isaacpit.news.api.service.NewsClient;
import com.isaacpit.news.api.service.client.http.NewsApiHttpClient;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Slf4j
@Builder
@Component
public class NewsApiClient implements NewsClient {

    private final NewsApiHttpClient newsApiHttpClient;

    private final ResponseDtoMapper responseDtoMapper = Mappers.getMapper(ResponseDtoMapper.class);

    @Override
    public NewsResponse callSearchApi(SearchRequest searchRequest) {
        NewsResponseDto responseDto = newsApiHttpClient.callSearchApi(searchRequest);

        return responseDtoMapper.mapNewsResponseToApiResponse(responseDto);
    }

    @Override
    public NewsResponse callTopHeadlinesApi(TopHeadlinesRequest requestParams) {
        NewsResponseDto responseDto = newsApiHttpClient.callTopHeadlinesApi(requestParams);

        return responseDtoMapper.mapNewsResponseToApiResponse(responseDto);
    }

}
