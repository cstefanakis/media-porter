package org.sda.mediaporter.models.multimedia.videoFiles;

import jakarta.persistence.*;
import org.sda.mediaporter.models.multimedia.Language;
import org.sda.mediaporter.models.multimedia.VideoCodec;

import java.util.List;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String year;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieAudioLanguage> audioLanguages;
    @ManyToMany
    @JoinTable(
            name = "movie_subtitle_language",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> subtitleLanguages;
    @ManyToOne
    @JoinColumn(name = "video_codec_id", nullable = true)
    private VideoCodec videoCodec;
    private String path;
}


