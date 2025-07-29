package org.sda.mediaporter.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuration {
    @Id
    private Long id;

    @Column(name = "max_dates_save_files")
    private Integer maxDatesSaveFile;

    @Column(name ="max_dates_control_files_from_external_sources")
    private Integer maxDatesControlFilesFromExternalSource;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_resolution",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "resolution_id")
    )
    private List<Resolution> videoResolutions;

    @Column(name = "first_video_bitrates_range")
    private Integer firstVideoBitrateValueRange;

    @Column(name = "second_video_bitrates_range")
    private Integer secondVideoBitrateValueRange;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_videoCodec",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "video_codec_id")
    )
    private List<Codec> videoCodecs;

    @Column(name = "first_audio_bitrates_range")
    private Integer firstAudioBitrateValueRange;

    @Column(name = "second_audio_bitrates_range")
    private Integer secondAudioBitrateValueRange;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_audioChannel",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "audio_channel_id")
    )
    private List<AudioChannel> audioChannels;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_audioCodec",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "audio_codec_id")
    )
    private List<Codec> audioCodecs;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_genre",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_language",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> audioLanguages;

    @Column(name = "first_video_sizes_range")
    private Double firstVideoSizeRange;

    @Column(name = "second_video_sizes_range")
    private Double secondVideoSizeRange;
}
