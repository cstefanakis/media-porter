package org.sda.mediaporter.dtos.theMovieDbDtos;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class TheMovieDbTvShowDto {
    private String title;
    private String originalTitle;
    private Integer year;
    private LocalDate firstAirDate;
    private LocalDate lastAirDate;
    private List<String> genres;
    private String homePage;
    private String languageCode;
    private List<String> countriesCodes;
    private String overview;
    private String poster;
    private String status;
    private Double rate;
    private Long theMovieDbId;
}
