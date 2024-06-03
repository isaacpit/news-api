package com.isaacpit.news.api.service.client.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaacpit.news.api.config.NewsWebClientConfig;
import com.isaacpit.news.api.domain.SearchRequest;
import com.isaacpit.news.api.domain.TopHeadlinesRequest;
import com.isaacpit.news.api.dto.GnewsResponseDto;
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

@Slf4j
@Builder
@Component
public class GnewsApiHttpClient {

    private final RestTemplate restTemplate;
    private final NewsWebClientConfig config;
    private final ObjectMapper objectMapper;

    @SneakyThrows({JsonProcessingException.class})
    public GnewsResponseDto callSearchApi(SearchRequest searchRequest) {
        log.info("calling gnews API with endpoint=%s".formatted(java.net.URLDecoder.decode(getSearchEndpointWithQueryParams(searchRequest, config.getGnews().getSearchApi().getApiPath()).toString(), StandardCharsets.UTF_8)));

        HttpEntity<Void> requestEntity = new HttpEntity<>(getHeaders());

        String response = restTemplate.exchange(getSearchEndpointWithQueryParams(searchRequest, config.getGnews().getSearchApi().getApiPath()), HttpMethod.GET, requestEntity, String.class).getBody();
        log.debug("gnews-search-response={}", response);
        return objectMapper.readValue(response, GnewsResponseDto.class);

    }

    @SneakyThrows({JsonProcessingException.class})
    public GnewsResponseDto callTopHeadlinesApi(TopHeadlinesRequest requestParams) {
        log.info("calling gnews API with endpoint=%s".formatted(java.net.URLDecoder.decode(getTopHeadlinesEndpointWithQueryParams(requestParams, config.getGnews().getTopHeadlinesApi().getApiPath()).toString(), StandardCharsets.UTF_8)));

        HttpEntity<Void> requestEntity = new HttpEntity<>(getHeaders());

        String response = restTemplate.exchange(getTopHeadlinesEndpointWithQueryParams(requestParams, config.getGnews().getTopHeadlinesApi().getApiPath()), HttpMethod.GET, requestEntity, String.class).getBody();
        log.debug("gnews-search-response={}", response);
        return objectMapper.readValue(response, GnewsResponseDto.class);

    }

    @SneakyThrows({URISyntaxException.class, MalformedURLException.class})
    private URI getSearchEndpointWithQueryParams(SearchRequest searchRequest, String path) {
        URIBuilder builder = new URIBuilder();

        NewsWebClientConfig.NewsWebClientConfigProps gnewsConfig = config.getGnews();

        builder.setScheme(gnewsConfig.getProtocol());
        builder.setHost(gnewsConfig.getHost());
        builder.setPath(path);

        // add static query params
        gnewsConfig.getSearchApi().getQueryParams().forEach(builder::addParameter);

        // add optional external API query params from controller request params
        Optional.ofNullable(searchRequest.getSearchIn()).ifPresent(val -> builder.addParameter(config.getGnews().getParamNames().getSearchInName(), val));

        // add dynamic query params passed from controller
        builder.addParameter(Commons.API_PARAM_KEYWORDS_QUERY, QueryBuilderUtil.buildKeywordsQueryFromNullables(searchRequest.getQuery(), searchRequest.getAuthor()));

        return builder.build().toURL().toURI();
    }

    @SneakyThrows({URISyntaxException.class, MalformedURLException.class})
    private URI getTopHeadlinesEndpointWithQueryParams(TopHeadlinesRequest requestParams, String path) {
        URIBuilder builder = new URIBuilder();

        NewsWebClientConfig.NewsWebClientConfigProps gnewsConfig = config.getGnews();

        builder.setScheme(gnewsConfig.getProtocol());
        builder.setHost(gnewsConfig.getHost());
        builder.setPath(path);

        // add static query params
        gnewsConfig.getTopHeadlinesApi().getQueryParams().forEach(builder::addParameter);

        // add optional query params
        Optional.ofNullable(requestParams.getQuery()).ifPresent(
                val -> builder.addParameter(Commons.API_PARAM_KEYWORDS_QUERY, QueryBuilderUtil.buildKeywordsQueryFromNullables(val, null))
        );
        Optional.ofNullable(requestParams.getPageNumber()).ifPresent(val -> builder.addParameter(config.getGnews().getParamNames().getPageNumberName(), val.toString()));
        Optional.ofNullable(requestParams.getPageSize()).ifPresent(val -> builder.addParameter(config.getGnews().getParamNames().getPageSizeName(), val.toString()));

        return builder.build().toURL().toURI();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        config.getCommonHeaders().forEach(httpHeaders::set);
        return httpHeaders;
    }

}
