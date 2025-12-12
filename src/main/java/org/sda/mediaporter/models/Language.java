package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "english_title", nullable = false)
    private String englishTitle;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(name = "iso_639_1")
    private String iso6391;

    @Column(name = "iso_639_2b")
    private String iso6392B;

    @Column(name = "iso_639_2t")
    private String iso6392T;

    @OneToMany(mappedBy = "language",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Audio> audios;

    @OneToMany(mappedBy = "language",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Subtitle> subtitles;

    @OneToMany(mappedBy = "originalLanguage",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Movie> movies;

    @ManyToMany(mappedBy = "languages")
    private List<TvShow> tvShows;
}
