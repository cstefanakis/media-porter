package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
    private String title;
    private String originalTitle;
    private Integer year;
    private Double rating;
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
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private List<Contributor> directors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_writers",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id")
    )
    private List<Contributor> writers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Contributor> actors = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String plot;
    private String country;
    private String poster;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_languages",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languages = new ArrayList<>();
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Video video;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Audio> audios = new ArrayList<>();
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Subtitle> subtitles = new ArrayList<>();
    private String path;
    private LocalDateTime modificationDate;

}
