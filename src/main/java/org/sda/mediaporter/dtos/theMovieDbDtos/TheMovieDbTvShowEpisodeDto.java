package org.sda.mediaporter.dtos.theMovieDbDtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class TheMovieDbTvShowEpisodeDto {
    private LocalDate airDate;
    private List<TheMovieDbCrewDto> writers;
    private List<TheMovieDbCrewDto> directors;
    private List<TheMovieDbCastDto> actors;
    private Integer episodeNumber;
    private String episodeType;
    private Integer seasonNumber;
    private String episodeName;
    private String overview;
    private Double rate;
    private String poster;
    private Long theMovieDbId;
    private Long tvShowTheMovieDbId;
}
