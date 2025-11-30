package org.sda.mediaporter.dtos.theMovieDbDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheMovieDbCrewDto {
    private String fullName;
    private String gender;
    private String poster;
    private Long theMovieDbId;
    private String department;
    private String job;
}
