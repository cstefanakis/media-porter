package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;

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
    private List<Resolution> videoResolutionsPrefer;
    @PositiveOrZero
    @Max(200000000)
    private Integer firstVideoBitrateValueRange;
    @Max(200000000)
    private Integer secondVideoBitrateValueRange;
    private List<Codec> videoCodecsPrefer;
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
    private List<Codec> audioCodecsPrefer;
    private List<Genre> genresPrefer;

    @PositiveOrZero
    @Max(31464320)
    private Double firstVideoSizeRangeRange;
    @Max(31457280)
    private Double secondVideoSizeRangeRange;
}
