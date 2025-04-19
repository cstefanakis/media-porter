package org.sda.mediaporter.models.multimedia.videoFiles;

import jakarta.persistence.*;
import org.sda.mediaporter.models.multimedia.AudioCodec;
import org.sda.mediaporter.models.multimedia.Language;

@Entity
public class TvShowAudioLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private TvShow tvShow;
    @ManyToOne
    private Language language;
    @ManyToOne
    private AudioCodec audioCodec;
}
