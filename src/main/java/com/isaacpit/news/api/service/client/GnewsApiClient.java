package com.isaacpit.news.api.service.client;

import com.isaacpit.news.api.domain.SearchRequestParams;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequestParams;
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
    public NewsResponse callSearchApi(SearchRequestParams searchRequestParams) {
        GnewsResponseDto responseDto = gnewsApiHttpClient.callSearchApi(searchRequestParams);

        return mapper.mapGnewsResponseToApiResponse(responseDto);
    }

    @Override
    public NewsResponse callTopHeadlinesApi(TopHeadlinesRequestParams requestParams) {
        GnewsResponseDto responseDto = gnewsApiHttpClient.callTopHeadlinesApi(requestParams);

        return mapper.mapGnewsResponseToApiResponse(responseDto);
    }

}
