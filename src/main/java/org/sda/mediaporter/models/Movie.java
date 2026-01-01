package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    // Many-to-Many relationship between Movie and Genre.
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    @JoinTable(
            name= "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    @JoinTable(
            name = "movie_directors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> directors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    @JoinTable(
            name = "movie_writers",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> writers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    @Builder.Default
    @JoinTable(
            name = "movie_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> actors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    @JoinTable(
            name = "movie_countries",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<Country> countries = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<VideoFilePath> videoFilePaths = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Character> characters = new ArrayList<>();
}
