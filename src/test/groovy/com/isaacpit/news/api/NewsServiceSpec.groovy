package com.isaacpit.news.api

import com.isaacpit.news.api.domain.NewsResponse
import com.isaacpit.news.api.domain.SearchRequest
import com.isaacpit.news.api.domain.TopHeadlinesRequest
import com.isaacpit.news.api.domain.enums.ApiClient
import com.isaacpit.news.api.repository.impl.InMemoryNewsRepository
import com.isaacpit.news.api.service.NewsClient
import com.isaacpit.news.api.service.NewsClientProvider
import com.isaacpit.news.api.service.NewsService
import com.isaacpit.news.api.service.client.NewsApiClient
import com.isaacpit.news.api.service.impl.NewsServiceWithCaching
import spock.lang.Specification
import spock.lang.Subject

class NewsServiceSpec extends Specification {

    @Subject
    NewsService newsService

    NewsClientProvider newsClientProvider
    InMemoryNewsRepository newsRepository

    NewsApiClient newsApiClient

    def setup() {

        newsClientProvider = Mock()
        newsRepository = Mock()

        newsApiClient = Mock()

        newsService = NewsServiceWithCaching.builder()
                .newsClientProvider(newsClientProvider)
                .newsRepository(newsRepository)
                .objectMapper(TestUtils.anObjectMapper())
                .build()
    }

    def "GET /search API - checks cache and calls API when does not have entry"() {
        when:
        def result = newsService.callSearchApiWithCaching(searchParams, client)


        then:
        result != null

        1 * newsRepository.getCachedResponse(_) >> Optional.empty()
        1 * newsClientProvider.getNewsClient(_) >> (NewsClient) newsApiClient
        1 * newsApiClient.callSearchApi(_) >> NewsResponse.builder().build()
        1 * newsRepository.put(_, _)

        where:
        searchParams                          | client
        SearchRequest.builder().build() | ApiClient.NEWS
    }

    def "GET /search API - checks cache and returns result when cache entry present "() {
        when:
        def result = newsService.callSearchApiWithCaching(searchParams, client)

        then:
        result != null

        1 * newsRepository.getCachedResponse(_) >> Optional.of(NewsResponse.builder().build())
        0 * newsClientProvider.getNewsClient(_) >> (NewsClient) newsApiClient
        0 * newsApiClient.callSearchApi(_) >> NewsResponse.builder().build()
        0 * newsRepository.put(_, _)

        where:
        searchParams                          | client
        SearchRequest.builder().build() | ApiClient.NEWS
    }

    def "GET /top-headlines API - checks cache and calls API when does not have entry"() {
        when:
        def result = newsService.callTopHeadlinesWithCaching(topHeadlineParams, client)


        then:
        result != null

        1 * newsRepository.getCachedResponse(_) >> Optional.empty()
        1 * newsClientProvider.getNewsClient(_) >> (NewsClient) newsApiClient
        1 * newsApiClient.callTopHeadlinesApi(_) >> NewsResponse.builder().build()
        1 * newsRepository.put(_, _)

        where:
        topHeadlineParams                          | client
        TopHeadlinesRequest.builder().build() | ApiClient.NEWS
    }

    def "GET /top-headliness API - checks cache and returns result when cache entry present "() {
        when:
        def result = newsService.callTopHeadlinesWithCaching(topHeadlineParams, client)

        then:
        result != null

        1 * newsRepository.getCachedResponse(_) >> Optional.of(NewsResponse.builder().build())
        0 * newsClientProvider.getNewsClient(_) >> (NewsClient) newsApiClient
        0 * newsApiClient.callTopHeadlinesApi(_) >> NewsResponse.builder().build()
        0 * newsRepository.put(_, _)

        where:
        topHeadlineParams                          | client
        TopHeadlinesRequest.builder().build() | ApiClient.NEWS
    }
}
