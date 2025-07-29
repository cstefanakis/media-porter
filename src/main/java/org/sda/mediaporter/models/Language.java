package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;

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

    @Column(name = "original_titles")
    private String originalTitle;

    @Column(name = "iso_639_1")
    private String iso6391;

    @Column(name = "iso_639_2b")
    private String iso6392B;

    @Column(name = "iso_639_2t")
    private String iso6392T;

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Audio> audios;

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Subtitle> subtitles;

    @ManyToMany(mappedBy = "languages", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private List<Movie> movies;
}
