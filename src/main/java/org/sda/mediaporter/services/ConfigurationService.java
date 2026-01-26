package org.sda.mediaporter.services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.AudioDto;
import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.dtos.VideoDto;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.services.fileServices.FileService;

import java.time.LocalDateTime;
import java.util.List;

public interface ConfigurationService {
    Configuration getConfigurationById(Long configurationId);
    List<Genre> getGenresFromConfiguration(Configuration configuration);
    List<Resolution> getVideoResolutionFromConfiguration(Configuration configuration);
    List<Codec> getVideoCodecsFromConfiguration(Configuration configuration);
    List<Codec> getAudioCodecsFromConfiguration(Configuration configuration);
    List<AudioChannel> getAudioChannelsFromConfiguration(Configuration configuration);
    List<Language> getLanguagesFromConfiguration(@Valid Configuration configuration);
    void updateConfiguration(Long configurationId, ConfigurationDto configurationDto);

    boolean isFileSupportSourceResolution(String videoResolution, SourcePath sourcePath);

    boolean isFileAudioCodecSupport(String audioCodec, SourcePath sourcePath);

    boolean isFileSupportVideoCodec(String videoCodec, SourcePath sourcePath);

    boolean isFileVideoBitrateInRange(Integer videoBitrate, SourcePath sourcePath);

    boolean isFileAudioChannelsSupport(Integer audioChannel, SourcePath sourcePath);

    boolean isFileAudioLanguageSupport(String audioLanguage, SourcePath sourcePath);

    boolean isFileAudioBitrateInRange(Integer audioBitrate, SourcePath sourcePath);

    boolean isFileSupportGenres(List <Genre> genres, SourcePath sourcePath);

    boolean isFileSupportFileSize(double fileSize, SourcePath sourcePath);

    boolean isFileModificationDateValid(LocalDateTime fileModificationDateTime, Integer validDatesBeforeNow);

    boolean isFileForCopy(VideoDto videoDto, List<AudioDto> audiosDto, List<Genre> movieGenres, SourcePath sourcePath, double fileSize, LocalDateTime fileModificationDateTime);
}
