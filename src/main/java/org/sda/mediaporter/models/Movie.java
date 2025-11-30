package org.sda.mediaporter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_title")
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(name = "year")
    private Integer year;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "poster")
    private String poster;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDateTime;

    @Column(name = "theMovieDb_id")
    private Long theMovieDbId;

    @ManyToOne
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    // Many-to-Many relationship between Movie and Genre.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name= "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_directors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> directors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_writers",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> writers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> actors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_countries",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<Country> countries;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoFilePath> videoFilePaths;
}
