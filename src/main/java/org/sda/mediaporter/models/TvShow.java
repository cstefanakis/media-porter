package org.sda.mediaporter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.metadata.Character;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "tvShows")
@NoArgsConstructor
@AllArgsConstructor
public class TvShow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "original_titles")
    private String originalTitle;

    @Column(name = "years")
    private Integer year;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "first_ait_date")
    private LocalDate firstAirDate;

    @Column(name = "last_Ait_Date")
    private LocalDate lastAirDate;

    @Column(name = "poster")
    private String poster;

    @Column(name = "overview",
            columnDefinition = "TEXT")
    private String overview;

    @Column(name = "last_modification_date")
    private LocalDateTime LastModificationDateTime;

    @Column(name = "home_pages")
    private String homePage;

    @Column(name = "status")
    private String status;

    @Column(name = "TheMovieDB_Id",
            unique = true)
    private Long theMoveDBTvShowId;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name= "tvShow_genres",
            joinColumns = @JoinColumn(name = "tvShow_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tvShow_countries",
            joinColumns = @JoinColumn(name = "tvShow_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<Country> countries;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tvShow_languages",
            joinColumns = @JoinColumn(name = "tvShow_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languages;

    @OneToMany(mappedBy = "tvShow",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<TvShowEpisode> tvShowEpisodes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tvShow_directors",
            joinColumns = @JoinColumn(name = "tvShow_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> directors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tvShow_writers",
            joinColumns = @JoinColumn(name = "tvShow_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> writers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tvShow_actors",
            joinColumns = @JoinColumn(name = "tvShow_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> actors;

    @OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Character> characters = new ArrayList<>();
}
