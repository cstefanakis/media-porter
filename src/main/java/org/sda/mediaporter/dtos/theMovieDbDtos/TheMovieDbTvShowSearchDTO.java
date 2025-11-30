package org.sda.mediaporter.dtos.theMovieDbDtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheMovieDbTvShowSearchDTO {
    private Long theMovieDbId;
    private String title;
    private String originalTitle;
    private String originalLanguage;
    private String overview;
    private Integer year;
    private Double rate;
    private String poster;
}
