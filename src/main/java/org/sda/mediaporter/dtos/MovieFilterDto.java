package org.sda.mediaporter.dtos;

import lombok.Getter;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.metadata.Subtitle;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MovieFilterDto {
    private String title;
    private Integer year;
    private Double rating;
    private Genre genre;
    private String country;
    private String director;
    private String actor;
    private Language audio;
    private String writer;
    private Language subtitle;
}
