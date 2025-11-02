package org.sda.mediaporter.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "tv-shows")
@NoArgsConstructor
@AllArgsConstructor
public class TvShow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tv_show_titles")
    private String title;

    @Column(name = "original_titles")
    private String originalTitle;

    @Column(name = "years")
    private Integer year;

    @Column(name = "ratings")
    private Double rating;

    @Column(name = "release_dates")
    private LocalDate releaseDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name= "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_countries",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "Country_id")
    )
    private List<Country> countries;

    @Column(name = "posters")
    private String poster;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_languages",
            joinColumns = @JoinColumn(name = "movie_ids"),
            inverseJoinColumns = @JoinColumn(name = "language_ids")
    )
    @JsonManagedReference("movieLanguages")
    private List<Language> languages;

    @Column(name = "home_Pages")
    private String homePage;

    @Column(name = "Statuses")
    private String status;

    @Column(name = "first_Ait_Dates")
    private LocalDate firstAirDate;

    @Column(name = "last_Ait_Dates")
    private LocalDate lastAirDate;

}
