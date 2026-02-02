package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

@Entity
@Table(name = "configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "max_dates_save_files")
    private Integer maxDatesSaveFile;

    @Column(name ="max_dates_control_files_from_external_sources")
    private Integer maxDatesControlFilesFromExternalSource;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference("videoResolutions")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference("videoCodecs")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference("audioChannels")
    @JoinTable(
            name = "configuration_audioChannel",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "audio_channel_id")
    )
    private List<AudioChannel> audioChannels;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference("audioCodecs")
    @JoinTable(
            name = "configuration_audioCodec",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "audio_codec_id")
    )
    private List<Codec> audioCodecs;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference("genres")
    @JoinTable(
            name = "configuration_genre",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference("configurationAudioLanguages")
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

    @OneToOne(
            mappedBy = "configuration",
            fetch = FetchType.LAZY
    )
    @JsonBackReference
    private SourcePath sourcePath;
}
