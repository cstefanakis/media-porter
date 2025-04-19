package org.sda.mediaporter.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String year;
    @ManyToMany
    @JoinTable(
            name = "movie_audio_language",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> audioLanguage;
    @ManyToMany
    @JoinTable(
            name = "movie_subtitle_language",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> subtitleLanguage;
    @ManyToOne
    @JoinColumn(name = "video_codec_id", nullable = true)
    private VideoCodec videoCodec;
    @ManyToOne
    @JoinColumn(name = "audio_codec_id", nullable = true)
    private AudioCodec audioCodec;
    private String folder;
    private String subfolder;
}


