package org.sda.mediaporter.dtos.theMovieDbDtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheMovieDbMovieSearchDTO {
    private String title;
    private String originalTitle;
    private String overview;
    private Long theMovieDbId;
    private String posterPath;
    private Integer year;
    private String originalLanguage;
}
