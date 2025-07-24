package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;

@Data
@Entity
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "movie_titles")
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
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_directors",
            joinColumns = @JoinColumn(name = "movie_ids"),
            inverseJoinColumns = @JoinColumn(name = "director_ids")
    )
    private List<Contributor> directors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_writers",
            joinColumns = @JoinColumn(name = "movie_ids"),
            inverseJoinColumns = @JoinColumn(name = "writer_ids")
    )
    private List<Contributor> writers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_actors",
            joinColumns = @JoinColumn(name = "movie_ids"),
            inverseJoinColumns = @JoinColumn(name = "actor_ids")
    )
    private List<Contributor> actors;

    @Column(name = "plots", columnDefinition = "TEXT")
    private String plot;

    @ManyToMany
    @JoinTable(
            name = "movie_countries",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "Country_id")
    )
    private List<Country> countries;

    @Column(name = "posters")
    private String poster;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_languages",
            joinColumns = @JoinColumn(name = "movie_ids"),
            inverseJoinColumns = @JoinColumn(name = "language_ids")
    )
    private List<Language> languages;

    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Video video;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Audio> audios;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Subtitle> subtitles = new ArrayList<>();

    @Column(name = "paths")
    private String path;

    @Column(name = "modification_dates")
    private LocalDateTime modificationDate;
}
