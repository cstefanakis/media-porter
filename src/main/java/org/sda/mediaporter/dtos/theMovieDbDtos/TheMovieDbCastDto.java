package org.sda.mediaporter.dtos.theMovieDbDtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheMovieDbCastDto {
    private String gender;
    private Long theMovieDbId;
    private String fullName;
    private String poster;
    private String character;
    private Integer order;
}
