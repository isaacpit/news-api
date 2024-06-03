package com.isaacpit.news.api.service.client;

import com.isaacpit.news.api.domain.SearchRequest;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequest;
import com.isaacpit.news.api.dto.GnewsResponseDto;
import com.isaacpit.news.api.mappers.ResponseDtoMapper;
import com.isaacpit.news.api.service.NewsClient;
import com.isaacpit.news.api.service.client.http.GnewsApiHttpClient;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Slf4j
@Builder
@Data
@Component
public class GnewsApiClient implements NewsClient {

    private final GnewsApiHttpClient gnewsApiHttpClient;
    private final ResponseDtoMapper mapper = Mappers.getMapper(ResponseDtoMapper.class);

    @Override
    public NewsResponse callSearchApi(SearchRequest searchRequest) {
        GnewsResponseDto responseDto = gnewsApiHttpClient.callSearchApi(searchRequest);

        return mapper.mapGnewsResponseToApiResponse(responseDto);
    }

    @Override
    public NewsResponse callTopHeadlinesApi(TopHeadlinesRequest requestParams) {
        GnewsResponseDto responseDto = gnewsApiHttpClient.callTopHeadlinesApi(requestParams);

        return mapper.mapGnewsResponseToApiResponse(responseDto);
    }

}
