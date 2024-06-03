package com.isaacpit.news.api.util;

import jakarta.annotation.Nullable;

import java.util.Optional;

public class QueryBuilderUtil {

    /**
     * construct the query value from multiple arguments. use the "OR" GNews /search API keyword to send multiple arguments when provided to the controller
     * query="political figure" author="terry brooks"   -> result='political figure OR isaac asimov'
     * query="political figure" author=null             -> result='political figure'
     * query=null author="terry brooks"                 -> result='terry brooks'
     */
    public static String buildKeywordsQueryFromNullables(@Nullable String query, @Nullable String author) {
        StringBuilder sb = new StringBuilder();
        Optional<String> optionalQuery = Optional.ofNullable(query);
        Optional<String> optionalAuthor = Optional.ofNullable(author);
        optionalQuery.ifPresent(q -> {
            sb.append(escapePhrase(q));
            // separate keywords and author query parameters if both are present
            optionalAuthor.ifPresent(ignore -> sb.append(Commons.KEYWORDS_OR_AUTHOR_SEPARATOR));
        });

        optionalAuthor.ifPresent(val -> sb.append(escapePhrase(val)));
        return sb.toString();
    }

    public static String escapePhrase(String phrase) {
        return phrase;
    }

}
