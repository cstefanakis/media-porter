package org.sda.mediaporter.models;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Movie {
    private String title;
    private Integer year;
    private Double rating;
    private LocalDate releaseDate;
    private List<String> genre;
    private List<String> directors;
    private List<String> writers;
    private List<String> actors;
    private String plot;
    private String country;
    private String poster;
}
