package com.isaacpit.news.api.mappers;


import com.isaacpit.news.api.domain.NewsResponse;
import com.isaacpit.news.api.dto.GnewsResponseDto;
import com.isaacpit.news.api.dto.NewsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ResponseDtoMapper {

    NewsResponse mapGnewsResponseToApiResponse(GnewsResponseDto sourc);

    @Mapping(source = "totalResults", target ="totalArticles")
    NewsResponse mapNewsResponseToApiResponse(NewsResponseDto source);

    @Mapping(source = "urlToImage", target ="image")
    NewsResponse.Article mapNewsResponseToApiResponse(NewsResponseDto.Article source);
}
