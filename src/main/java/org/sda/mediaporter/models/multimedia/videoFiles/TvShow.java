package org.sda.mediaporter.models.multimedia.videoFiles;

import jakarta.persistence.*;
import org.sda.mediaporter.models.multimedia.AudioCodec;
import org.sda.mediaporter.models.multimedia.Language;
import org.sda.mediaporter.models.multimedia.VideoCodec;

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
    @OneToMany(mappedBy = "tvshow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TvShowAudioLanguage> audioLanguages;
    @ManyToMany
    @JoinTable(
            name = "tvshow_subtitle_languages",
            joinColumns = @JoinColumn(name = "tvshow_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> subtitleLanguages;
    @ManyToOne
    @JoinColumn(name = "video_codec_id", nullable = true)
    private VideoCodec videoCodec;
    private String path;
}
