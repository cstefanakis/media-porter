package org.sda.mediaporter.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CountryDto {
    private String iso2Code;
    private String iso3Code;
    private String englishName;
    private String nativeName;
}
