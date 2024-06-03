package com.isaacpit.news.api.controller;

import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.exception.InvalidNewsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class NewsApiControllerAdvice {

    @ExceptionHandler(value = {HttpClientErrorException.class})
    public ResponseEntity<NewsResponse> handleClientExceptionError(HttpClientErrorException e) {
        log.error("Client error occurred:", e);

        // !! should probably mask the client's error messages in case our API key is sent in error message
        return new ResponseEntity<>(
                NewsResponse.builder()
                        .error(NewsResponse.Error.builder()
                                .errorMessage(e.getMessage())
                                .errorStatusCode(e.getStatusCode().value())
                                .build())
                        .build()
                , new HttpHeaders(), e.getStatusCode());
    }

    @ExceptionHandler(value = {InvalidNewsClient.class})
    public ResponseEntity<NewsResponse> handleInvalidClientException(InvalidNewsClient e) {
        log.error("Invalid client in argument list:", e);
        return new ResponseEntity<>(
                NewsResponse.builder()
                        .error(NewsResponse.Error.builder()
                                .errorMessage(e.getMessage())
                                .errorStatusCode(HttpStatus.BAD_REQUEST.value())
                                .build())
                        .build()
                , new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
