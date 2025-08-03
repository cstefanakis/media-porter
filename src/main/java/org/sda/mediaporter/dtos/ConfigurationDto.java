package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

@Getter
@Builder
//@NoArgsConstructor
public class ConfigurationDto {
    private Long id;

    @PositiveOrZero(message = "maxDatesSaveFile must be zero or positive")
    @Max(value = 9000, message = "maxDatesSaveFile must be less than or equal to 9000")
    private Integer maxDatesSaveFile;

    @Max(value = 9000, message = "maxDatesControlFilesFromExternalSource must be less than or equal to 9000")
    private Integer maxDatesControlFilesFromExternalSource;

    private List<Resolution> videoResolutions;

    @PositiveOrZero(message = "firstVideoBitrateValueRange must be zero or positive")
    @Max(value = 200000000, message = "firstVideoBitrateValueRange must be less than or equal to 200000000")
    private Integer firstVideoBitrateValueRange;

    @PositiveOrZero(message = "secondVideoBitrateValueRange must be zero or positive")
    @Max(value = 200000000, message = "secondVideoBitrateValueRange must be less than or equal to 200000000")
    private Integer secondVideoBitrateValueRange;

    private List<Codec> videoCodecs;

    @PositiveOrZero(message = "firstAudioBitrateValueRange must be zero or positive")
    @Max(value = 2048000, message = "firstAudioBitrateValueRange must be less than or equal to 2048000")
    private Integer firstAudioBitrateValueRange;

    @PositiveOrZero(message = "secondAudioBitrateValueRange must be zero or positive")
    @Max(value = 2048000, message = "secondAudioBitrateValueRange must be less than or equal to 2048000")
    private Integer secondAudioBitrateValueRange;

    private List<AudioChannel> audioChannels;

    private List<Codec> audioCodecs;

    private List<Genre> genres;

    private List<Language> languages;

    @PositiveOrZero(message = "firstVideoSizeRange must be zero or positive")
    @Max(value = 31464320, message = "firstVideoSizeRange must be less than or equal to 31464320")
    private Double firstVideoSizeRange;

    @PositiveOrZero(message = "secondVideoSizeRange must be zero or positive")
    @Max(value = 31464320, message = "secondVideoSizeRange must be less than or equal to 31464320")
    private Double secondVideoSizeRange;
}
