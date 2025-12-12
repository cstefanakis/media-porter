package org.sda.mediaporter.dtos.theMovieDbDtos;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class TheMovieDbMovieDto {
    private String title;
    private String originalTitle;
    private Integer year;
    private List<String> genres;
    private String homePage;
    private Long theMoveDbId;
    private List<String> countries;
    private String languageCode;
    private String overview;
    private String poster;
    private String status;
    private Double rate;
    private LocalDate releaseDate;
}
