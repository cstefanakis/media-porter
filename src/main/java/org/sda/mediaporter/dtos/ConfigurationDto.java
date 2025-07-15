package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

@Getter
@NoArgsConstructor
public class ConfigurationDto {
    private Long id;
    private Integer maxDatesSaveFile;
    private Integer maxDatesControlFilesFromExternalSource;
    private List<Resolution> videoResolutions;
    private Integer firstVideoBitrateValueRange;
    private Integer secondVideoBitrateValueRange;
    private List<Codec> videoCodecs;
    private Integer firstAudioBitrateValueRange;
    private Integer secondAudioBitrateValueRange;
    private List<AudioChannel> audioChannels;
    private List<Codec> audioCodecs;
    private List<Genre> genres;
    private List<Language> languages;
    private Double firstVideoSizeRange;
    private Double secondVideoSizeRange;
}
