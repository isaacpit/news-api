package com.isaacpit.news.api.service.client.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaacpit.news.api.config.NewsWebClientConfig;
import com.isaacpit.news.api.domain.SearchRequest;
import com.isaacpit.news.api.domain.TopHeadlinesRequest;
import com.isaacpit.news.api.dto.NewsResponseDto;
import com.isaacpit.news.api.util.Commons;
import com.isaacpit.news.api.util.QueryBuilderUtil;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.isaacpit.news.api.util.Commons.*;

@Slf4j
@Builder
@Component
public class NewsApiHttpClient {

    private final RestTemplate restTemplate;
    private final NewsWebClientConfig config;
    private final ObjectMapper objectMapper;

    @SneakyThrows({JsonProcessingException.class})
    public NewsResponseDto callSearchApi(SearchRequest searchRequest) {
        log.info("calling news API with endpoint=%s".formatted(java.net.URLDecoder.decode(getSearchEndpointWithQueryParams(searchRequest).toString(), StandardCharsets.UTF_8)));

        HttpEntity<Void> requestEntity = new HttpEntity<>(getHeaders(config.getNews().getApikey()));

        String response = restTemplate.exchange(getSearchEndpointWithQueryParams(searchRequest), HttpMethod.GET, requestEntity, String.class).getBody();
        log.debug("gews-search-response={}", response);
        return objectMapper.readValue(response, NewsResponseDto.class);

    }

    @SneakyThrows({JsonProcessingException.class})
    public NewsResponseDto callTopHeadlinesApi(TopHeadlinesRequest requestParams) {
        log.info("calling news API with endpoint=%s".formatted(java.net.URLDecoder.decode(getTopHeadlineEndpointWithQueryParams(requestParams).toString(), StandardCharsets.UTF_8)));

        HttpEntity<Void> requestEntity = new HttpEntity<>(getHeaders(config.getNews().getApikey()));

        String response = restTemplate.exchange(getTopHeadlineEndpointWithQueryParams(requestParams), HttpMethod.GET, requestEntity, String.class).getBody();
        log.debug("gews-search-response={}", response);
        return objectMapper.readValue(response, NewsResponseDto.class);

    }


    @SneakyThrows({URISyntaxException.class, MalformedURLException.class})
    private URI getSearchEndpointWithQueryParams(SearchRequest searchRequest) {
        URIBuilder builder = new URIBuilder();

        NewsWebClientConfig.NewsWebClientConfigProps newsConfigProps = config.getNews();

        builder.setScheme(newsConfigProps.getProtocol());
        builder.setHost(newsConfigProps.getHost());
        builder.setPath(newsConfigProps.getSearchApi().getApiPath());

        // sets query value from optional nullable
        builder.addParameter(API_PARAM_KEYWORDS_QUERY, QueryBuilderUtil.buildKeywordsQueryFromNullables(searchRequest.getQuery(), searchRequest.getAuthor()));

        // sets the locations to search in, if present
        Optional.ofNullable(searchRequest.getSearchIn()).ifPresent(searchIn -> builder.addParameter(config.getNews().getParamNames().getSearchInName(), searchIn));

        newsConfigProps.getSearchApi().getQueryParams().forEach(builder::addParameter);
        return builder.build().toURL().toURI();
    }

    @SneakyThrows({URISyntaxException.class, MalformedURLException.class})
    private URI getTopHeadlineEndpointWithQueryParams(TopHeadlinesRequest requestParams) {
        URIBuilder builder = new URIBuilder();

        NewsWebClientConfig.NewsWebClientConfigProps newsConfigProps = config.getNews();

        builder.setScheme(newsConfigProps.getProtocol());
        builder.setHost(newsConfigProps.getHost());
        builder.setPath(newsConfigProps.getTopHeadlinesApi().getApiPath());

        // add static query params
        newsConfigProps.getTopHeadlinesApi().getQueryParams().forEach(builder::addParameter);

        // add optional query params
        Optional.ofNullable(requestParams.getQuery()).ifPresent(
                val -> builder.addParameter(Commons.API_PARAM_KEYWORDS_QUERY, QueryBuilderUtil.buildKeywordsQueryFromNullables(val, null))
        );

        Optional.ofNullable(requestParams.getPageNumber()).ifPresent(val -> builder.addParameter(newsConfigProps.getParamNames().getPageNumberName(), val.toString()));
        Optional.ofNullable(requestParams.getPageSize()).ifPresent(val -> builder.addParameter(newsConfigProps.getParamNames().getPageSizeName(), val.toString()));

        return builder.build().toURL().toURI();
    }

    private HttpHeaders getHeaders(String apikey) {
        HttpHeaders httpHeaders = new HttpHeaders();
        config.getCommonHeaders().forEach(httpHeaders::set);
        httpHeaders.add(HEADER_API_KEY, apikey);
        return httpHeaders;
    }

}

