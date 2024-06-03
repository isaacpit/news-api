package com.isaacpit.news.api.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaacpit.news.api.domain.SearchRequestParams;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequestParams;
import com.isaacpit.news.api.dto.GnewsResponseDto;
import com.isaacpit.news.api.dto.NewsResponseDto;
import com.isaacpit.news.api.mappers.ResponseDtoMapper;
import com.isaacpit.news.api.service.NewsClient;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import static com.isaacpit.news.api.util.Commons.*;


/**
 * mock news client that serves a local json file as a response
 */
@Slf4j
@Builder
@Component
public class MockApiClient implements NewsClient {

    private ObjectMapper objectMapper;

    private final ResponseDtoMapper mapper = Mappers.getMapper(ResponseDtoMapper.class);

    /**
     * ignores query param at the moment, just using a single static file for some testing
     */
    @Override
    @SneakyThrows({JsonProcessingException.class, IOException.class})
    public NewsResponse callSearchApi(SearchRequestParams searchRequestParams) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(MOCK_SEARCH_RESPONSE_FILEPATH)) {
            GnewsResponseDto responseDto = objectMapper.readValue(inputStream, GnewsResponseDto.class);
            return mapper.mapGnewsResponseToApiResponse(responseDto);
        }
    }

    @Override
    @SneakyThrows({JsonProcessingException.class, IOException.class})
    public NewsResponse callTopHeadlinesApi(TopHeadlinesRequestParams requestParams) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(MOCK_TOP_HEADLINES_RESPONSE_FILEPATH)) {
            NewsResponseDto responseDto = objectMapper.readValue(inputStream, NewsResponseDto.class);
            return mapper.mapNewsResponseToApiResponse(responseDto);
        }
    }

}
