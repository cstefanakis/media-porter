package org.sda.mediaporter.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class TvShow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private int year;
    private int season;
    private int episode;
    @ManyToMany
    @JoinTable(
            name = "tvshow_audio_languages",
            joinColumns = @JoinColumn(name = "tvshow_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> audioLanguages;
    @ManyToMany
    @JoinTable(
            name = "tvshow_subtitle_languages",
            joinColumns = @JoinColumn(name = "tvshow_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> subtitleLanguages;

}
