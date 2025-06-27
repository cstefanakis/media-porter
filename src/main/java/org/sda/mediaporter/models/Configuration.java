package org.sda.mediaporter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.metadata.Codec;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuration {
    @Id
    private Long id;
    private Integer maxDatesSaveFile;
    private Integer maxDatesControlFilesFromExternalSource;

    private Integer firstVideoResolutionValueRange;
    private Integer secondVideoResolutionValueRange;

    private Integer firstVideoBitrateValueRange;
    private Integer secondVideoBitrateValueRange;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_videoCodec",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "video_codec_id")
    )
    private List<Codec> videoCodecsPrefer;
    private Integer firstAudioBitrateValueRange;
    private Integer secondAudioBitrateValueRange;
    private Integer firstAudioChannelsValueRange;
    private Integer secondAudioChannelsValueRange;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_audioCodec",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "audio_codec_id")
    )
    private List<Codec> audioCodecsPrefer;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_genre",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genresPrefer;
    private Double firstVideoSizeRangeRange;
    private Double secondVideoSizeRangeRange;
}
