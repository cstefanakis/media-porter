package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieUpdateDto {

    @NotEmpty(message = "Movie title must not be empty")
    String title;

    Integer year;
}
