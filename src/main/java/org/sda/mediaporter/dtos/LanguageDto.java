package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LanguageDto {
    @NotEmpty(message = "English title must not be empty")
    private String englishTitle;

    private String originalTitle;

    private String iso6391;

    private String iso6392B;

    private String iso6392T;
}
