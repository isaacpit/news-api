package com.isaacpit.news.api.domain.enums;


import lombok.Getter;

import static com.isaacpit.news.api.domain.enums.ApiClient.Constants.*;

@Getter
public enum ApiClient {
    GNEWS(GNEWS_VALUE, GNEWS_VALUE.toLowerCase() ),
    NEWS(NEWS_VALUE, NEWS_VALUE.toLowerCase() ),
    MOCK(Constants.MOCK_VALUE, MOCK_VALUE.toLowerCase());

    final String name;
    final String value;

    ApiClient(String name, String value) {
        this.name = name;
        this.value= value;
    }

    // allows usage of a constant in @RequestParam.defaultValue since ApiClient.GNEWS.name() and ApiClient.GNEWS.toString() cause errors
    public static class Constants {
        public static final String GNEWS_VALUE = "GNEWS";
        public static final String NEWS_VALUE = "NEWS";
        public static final String MOCK_VALUE = "MOCK";
    }
}
