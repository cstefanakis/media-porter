package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ConfigurationDto {
    private Long id;
    @PositiveOrZero
    @Max(9000)
    private Integer maxDatesSaveFile;
    @Max(9000)
    private Integer maxDatesControlFilesFromExternalSource;
    @PositiveOrZero
    @Max(15360)
    private Integer firstVideoResolutionValueRange;
    @Max(15360)
    private Integer secondVideoResolutionValueRange;
    @PositiveOrZero
    @Max(200000000)
    private Integer firstVideoBitrateValueRange;
    @Max(200000000)
    private Integer secondVideoBitrateValueRange;
    private List<String> videoCodecsPrefer;
    @PositiveOrZero
    @Max(2048000)
    private Integer firstAudioBitrateValueRange;
    @Max(2048000)
    private Integer secondAudioBitrateValueRange;
    @PositiveOrZero
    @Max(24)
    private Integer firstAudioChannelsValueRange;
    @Max(24)
    private Integer secondAudioChannelsValueRange;
    private List<String> audioCodecsPrefer;
    private List<String> genresPrefer;

    @PositiveOrZero
    @Max(31464320)
    private Double firstVideoSizeRangeRange;
    @Max(31457280)
    private Double secondVideoSizeRangeRange;
}
