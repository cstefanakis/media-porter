package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CountryDto {
    @NotEmpty(message = "ISO 2 code must not be empty")
    private String iso2Code;

    @NotEmpty(message = "ISO 3 code must not be empty")
    private String iso3Code;

    @NotEmpty(message = "English name must not be empty")
    private String englishName;

    @NotEmpty(message = "Native name must not be empty")
    private String nativeName;
}
