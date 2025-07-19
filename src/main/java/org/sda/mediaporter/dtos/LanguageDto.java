package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LanguageDto {
    private String englishTitle;
    private String originalTitle;
    private String iso6391;
    private String iso6392B;
    private String iso6392T;
}
