package org.sda.mediaporter.dtos;

import lombok.Getter;

@Getter
public class CountryDto {
    private String iso2Code;
    private String iso3Code;
    private String englishName;
    private String nativeName;
}
