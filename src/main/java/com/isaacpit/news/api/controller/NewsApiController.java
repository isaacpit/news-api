package com.isaacpit.news.api.controller;

import com.isaacpit.news.api.domain.SearchRequest;
import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.domain.TopHeadlinesRequest;
import com.isaacpit.news.api.domain.enums.ApiClient;
import com.isaacpit.news.api.exception.InvalidNewsClient;
import com.isaacpit.news.api.service.NewsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.bind.annotation.*;

@Builder
@RestController
@RequestMapping("/news/api")
public class NewsApiController {

    private final NewsService newsService;

    @GetMapping("/search")
    public NewsResponse search(@RequestParam(required = false) @Size(max = 1000) String query,
                               @RequestParam(required = false) @Size(max = 100) String author,
                               @RequestParam(required = false) @Size(max = 50)
                               String searchIn,
                               @RequestParam(name = "client",
                                       defaultValue = ApiClient.Constants.NEWS_VALUE) String apiClientString) {

        ApiClient apiClient = tryParsingClientEnum(apiClientString);

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .author(author)
                .searchIn(searchIn)
                .build();

        return newsService.callSearchApiWithCaching(searchRequest, apiClient);
    }

    @GetMapping("/top-headlines")
    public NewsResponse fetchTopHeadlines(@RequestParam(required = false)
                                          @Size(max = 1000)
                                                String query,
                                          @RequestParam(name = "page", defaultValue = "1")
                                          @Min(value = 1)
                                                Integer pageNumber,
                                          @RequestParam(defaultValue = "10")
                                          @Max(value = 100)
                                          @Min(value = 1)
                                                Integer pageSize,
                                          @RequestParam(name = "client", defaultValue = ApiClient.Constants.NEWS_VALUE)
                                                String apiClientString) {

        ApiClient apiClient = tryParsingClientEnum(apiClientString);

        TopHeadlinesRequest requestParams = TopHeadlinesRequest.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .query(query)
                .build();

        return newsService.callTopHeadlinesWithCaching(requestParams, apiClient);
    }

    private static ApiClient tryParsingClientEnum(String apiClientString) {
        try {
            return ApiClient.valueOf(apiClientString.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidNewsClient("Invalid news client: '%s'".formatted(apiClientString));
        }
    }
}
