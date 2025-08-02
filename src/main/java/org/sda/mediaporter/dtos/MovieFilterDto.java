package org.sda.mediaporter.dtos;

import lombok.Builder;
import lombok.Getter;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.Country;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.metadata.Subtitle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MovieFilterDto {
    private String title;
    private Integer year;
    private Double rating;
    private List <Long> genreIds;
    private List <Long> countryIds;
    private List <Long> aLanguageIds;
}
