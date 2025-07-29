package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenreDto {

    @NotEmpty(message = "Title must not be empty")
    private String title;
}
