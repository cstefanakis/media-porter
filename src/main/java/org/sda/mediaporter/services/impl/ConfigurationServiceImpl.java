package org.sda.mediaporter.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.dtos.AudioDto;
import org.sda.mediaporter.dtos.VideoDto;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.services.ConfigurationService;
import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.GenreRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final GenreRepository genreRepository;
    private final CodecRepository codecRepository;
    private final LanguageRepository languageRepository;
    private final ResolutionRepository resolutionRepository;
    private final AudioChannelRepository audioChannelRepository;
    private final SourcePathService sourcePathService;


    private double maxFileSize = 31457280;
    private int maxVideoBitrate = 200000000;
    private int maxAudioBitrate = 2048000;
    private int maxDaysToPast = 9000;

    @Autowired
    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository, GenreRepository genreRepository, CodecRepository codecRepository, LanguageRepository languageRepository, ResolutionRepository resolutionRepository, AudioChannelRepository audioChannelRepository, SourcePathService sourcePathService) {
        this.configurationRepository = configurationRepository;
        this.genreRepository = genreRepository;
        this.codecRepository = codecRepository;
        this.languageRepository = languageRepository;
        this.resolutionRepository = resolutionRepository;
        this.audioChannelRepository = audioChannelRepository;
        this.sourcePathService = sourcePathService;
    }

    @Override
    public Configuration getConfigurationById(Long configurationId) {
        return configurationRepository.findById(configurationId).orElseThrow(() -> new EntityNotFoundException(String.format("Configuration with id: %s not exist", configurationId)));
    }

    @Override
    public List<Genre> getGenresFromConfiguration(Configuration configuration) {
        return configurationRepository.findGenresFromConfiguration(configuration);
    }

    @Override
    public List<Resolution> getVideoResolutionFromConfiguration(Configuration configuration) {
        return configurationRepository.findVideoResolutionsFromConfiguration(configuration);
    }

    @Override
    public List<Codec> getVideoCodecsFromConfiguration(Configuration configuration) {
        return configurationRepository.findVideoCodecsFromConfiguration(configuration);
    }

    @Override
    public List<Codec> getAudioCodecsFromConfiguration(Configuration configuration) {
        return configurationRepository.findAudioCodecsFromConfiguration(configuration);
    }

    @Override
    public List<AudioChannel> getAudioChannelsFromConfiguration(Configuration configuration) {
        return configurationRepository.findAudioChannelsFromConfiguration(configuration);
    }

    @Override
    public List<Language> getLanguagesFromConfiguration(Configuration configuration) {
        return configurationRepository.findLanguagesFromConfiguration(configuration);
    }

    @Override
    public void updateConfiguration(Long configurationId, ConfigurationDto configurationDto) {
        Configuration configuration = getConfigurationById(configurationId);
        configurationRepository.save(toEntity(configuration, configurationDto));
    }

    @Override
    public boolean isFileSupportSourceResolution(String videoResolution, SourcePath sourcePath) {
        return configurationRepository.isFileSupportSourceResolution(videoResolution, sourcePath);
    }

    @Override
    public boolean isFileAudioCodecSupport(String audioCodec, SourcePath sourcePath) {
        return configurationRepository.isFileAudioCodecSupport(audioCodec, sourcePath);
    }

    @Override
    public boolean isFileSupportVideoCodec(String videoCodec, SourcePath sourcePath) {
        return configurationRepository.isFileSupportVideoCodec(videoCodec, sourcePath);
    }

    @Override
    public boolean isFileVideoBitrateInRange(Integer videoBitrate, SourcePath sourcePath) {
        return configurationRepository.isFileVideoBitrateInRange(videoBitrate, sourcePath);
    }

    @Override
    public boolean isFileAudioChannelsSupport(Integer audioChannel, SourcePath sourcePath) {
        return configurationRepository.isFileAudioChannelsSupport(audioChannel, sourcePath);
    }

    @Override
    public boolean isFileAudioLanguageSupport(String audioLanguage, SourcePath sourcePath) {
        return configurationRepository.isFileAudioLanguageSupport(audioLanguage, sourcePath);
    }

    @Override
    public boolean isFileAudioBitrateInRange(Integer audioBitrate, SourcePath sourcePath) {
        return configurationRepository.isFileAudioBitrateInRange(audioBitrate, sourcePath);
    }

    @Override
    public boolean isFileSupportGenres(List<Genre> genres, SourcePath sourcePath) {
        for (Genre genre : genres){
            boolean result = configurationRepository.isFileSupportGenres(genre, sourcePath);
            if(!result){
                return false;
            }
        }return true;
    }

    @Override
    public boolean isFileSupportFileSize(double fileSize, SourcePath sourcePath) {
        Configuration configuration = sourcePath.getConfiguration();
        Double firstFileSizeLimit = configuration.getFirstVideoSizeRange();
        Double secondFileSizeLimit = configuration.getSecondVideoSizeRange();
        return (firstFileSizeLimit == null && secondFileSizeLimit == null)
                || (firstFileSizeLimit == null && fileSize <= secondFileSizeLimit)
                || (fileSize >= firstFileSizeLimit && secondFileSizeLimit == null)
                || (fileSize >= firstFileSizeLimit && fileSize <= secondFileSizeLimit);
    }

    @Override
    public boolean isFileModificationDateValid(LocalDateTime fileModificationDateTime, Integer validDatesBeforeNow) {
        long datesBeforeNow = ChronoUnit.DAYS.between(
                fileModificationDateTime,
                LocalDateTime.now()
        );
        return validDatesBeforeNow == null || datesBeforeNow <= validDatesBeforeNow;
    }

    @Override
    public boolean isFileForCopy(VideoDto videoDto, List<AudioDto> audiosDto, List<Genre> genres, SourcePath sourcePath, double fileSize, LocalDateTime fileModificationDateTime) {
            boolean result = true;
            Configuration configuration = sourcePath.getConfiguration();
            Integer validDatesBeforeNow = configuration.getMaxDatesSaveFile();

            for (AudioDto audioDto : audiosDto) {
                boolean audioCodecSupport = isFileAudioCodecSupport(audioDto.getAudioCodec(), sourcePath);
                boolean audioChannelsSupport = isFileAudioChannelsSupport(audioDto.getAudioChannel(), sourcePath);
                boolean languageSupport = isFileAudioLanguageSupport(audioDto.getAudioLanguage(), sourcePath);
                boolean audioBitrateSupport = isFileAudioBitrateInRange(audioDto.getAudioBitrate(), sourcePath);
                if(!audioBitrateSupport || !audioCodecSupport || !audioChannelsSupport || !languageSupport){
                    result = false;
                }
            }
            boolean resolutionSupport = isFileSupportSourceResolution(videoDto.getVideoResolution(), sourcePath);
            boolean videoCodec = isFileSupportVideoCodec(videoDto.getVideoCodec(), sourcePath);
            boolean videoBitrate = isFileVideoBitrateInRange(videoDto.getVideoBitrate(), sourcePath);
            boolean genre = isFileSupportGenres(genres, sourcePath);
            boolean fileSizeSupport = isFileSupportFileSize(fileSize, sourcePath);
            boolean validModificationDate = isFileModificationDateValid(fileModificationDateTime, validDatesBeforeNow);
            if(!resolutionSupport || !videoCodec || !videoBitrate || !genre || !fileSizeSupport || !validModificationDate) {result = false;}
            return result;
    }

    @Override
    public boolean isFileOld(LocalDateTime fileModificationDateTime, Integer maxDatesSaveFile) {
        if(maxDatesSaveFile == null) {return false;}
        LocalDateTime deleteDates = LocalDateTime.now().minusDays(maxDatesSaveFile);
        return fileModificationDateTime.isBefore(deleteDates);
    }

    @Override
    public Configuration updateConfigurationBySourcePathId(Long id, ConfigurationDto configurationDto) {
        Optional<Configuration> configurationOptional = configurationRepository.findConfigurationBySourcePathId(id);
        if(configurationOptional.isPresent()){
            Configuration configuration = configurationOptional.get();
            return configurationRepository.save(toEntity(configuration, configurationDto));
        }
        return null;
    }


    private Configuration toEntity(Configuration configuration, ConfigurationDto configurationDto) {
        return Configuration.builder()
                .id(configuration.getId())
                .maxDatesSaveFile(configurationDto.getMaxDatesSaveFile() == null
                        ? this.maxDaysToPast
                        : configurationDto.getMaxDatesSaveFile())
                .maxDatesControlFilesFromExternalSource(configurationDto.getMaxDatesControlFilesFromExternalSource() == null
                        ? 0
                        : configurationDto.getMaxDatesControlFilesFromExternalSource())
                .videoResolutions(validatedResolutions(configurationDto, configuration))
                .firstVideoBitrateValueRange(validatedFirstVideoBitrateValueRange(configurationDto, configuration))
                .secondVideoBitrateValueRange(validatedSecondVideoBitrateValueRange(configurationDto, configuration))
                .videoCodecs(validatedCodecs(configurationDto.getVideoCodecIds(), MediaTypes.VIDEO, configuration.getVideoCodecs()))
                .firstAudioBitrateValueRange(validatedFirstAudioBitrateValueRange(configurationDto, configuration))
                .secondAudioBitrateValueRange(validatedSecondAudioBitrateValueRange(configurationDto, configuration))
                .audioChannels(validatedAudioChannels(configurationDto, configuration))
                .audioCodecs(validatedCodecs(configurationDto.getAudioCodecIds(), MediaTypes.AUDIO, configuration.getAudioCodecs()))
                .genres(validatedGenres(configurationDto, configuration))
                .audioLanguages(validatedLanguages(configurationDto, configuration))
                .firstVideoSizeRange(validatedFirstVideoSizeRangeRange(configurationDto, configuration))
                .secondVideoSizeRange(validatedSecondVideoSizeRangeRange(configurationDto, configuration))
                .sourcePath(configuration.getSourcePath())
                .build();
    }

    private Integer validatedFirstVideoBitrateValueRange(ConfigurationDto configurationDto, Configuration configuration){
        Integer videoBitrateDto = configurationDto.getFirstVideoBitrateValueRange();
        Integer videoBitrate = configuration.getFirstVideoBitrateValueRange();

        if(videoBitrateDto == null && videoBitrate == null){
            return 0;
        }
        if(videoBitrateDto == null){
            return videoBitrate;
        }
        return videoBitrateDto;
    }

    private Integer validatedSecondVideoBitrateValueRange(ConfigurationDto configurationDto, Configuration configuration){
        Integer secondVideoBitrateDto = configurationDto.getSecondVideoBitrateValueRange();
        Integer secondVideoBitrate = configuration.getSecondVideoBitrateValueRange();
        if(secondVideoBitrateDto == null && secondVideoBitrate == null){
            return 0;
        }
        if(secondVideoBitrateDto == null){
            return secondVideoBitrate;
        }
        if(secondVideoBitrate > maxVideoBitrate){
            return maxVideoBitrate;
        }
        Integer firstVideoBitrate = configuration.getFirstVideoBitrateValueRange();
        if(secondVideoBitrateDto < firstVideoBitrate){
            throw new RuntimeException("Second bitrate value can't be smaller than first bitrate value");
        }
        return secondVideoBitrateDto;
    }

    private Integer validatedFirstAudioBitrateValueRange(ConfigurationDto configurationDto, Configuration configuration){
        Integer firstAudioBitrateDto = configurationDto.getFirstAudioBitrateValueRange();
        Integer firstAudioBitrate = configuration.getFirstAudioBitrateValueRange();

        if(firstAudioBitrateDto == null && firstAudioBitrate == null){
            return 0;
        }
        if(firstAudioBitrateDto == null){
            return firstAudioBitrate;
        }
        return firstAudioBitrateDto;
    }

    private Integer validatedSecondAudioBitrateValueRange(ConfigurationDto configurationDto, Configuration configuration){
        Integer secondAudioBitrateDto = configurationDto.getSecondAudioBitrateValueRange();
        Integer secondAudioBitrate = configuration.getSecondAudioBitrateValueRange();
        if(secondAudioBitrateDto == null && secondAudioBitrate == null) {
            return maxAudioBitrate;
        }
        if(secondAudioBitrateDto == null) {
            return secondAudioBitrate;
        }
        if(secondAudioBitrateDto > maxAudioBitrate){
            return maxAudioBitrate;
        }
        Integer firstAudioBitrate = configuration.getFirstAudioBitrateValueRange();
        if(secondAudioBitrateDto < firstAudioBitrate){
            throw new RuntimeException("Second bitrate value can't be smaller than first bitrate value");
        }
        return secondAudioBitrateDto;
    }

    private Double validatedFirstVideoSizeRangeRange(ConfigurationDto configurationDto, Configuration configuration){
        Double firstVideoSizeDto = configurationDto.getFirstVideoSizeRange();
        Double firstVideoSize = configuration.getFirstVideoSizeRange();
        if(firstVideoSizeDto == null && firstVideoSize == null){
            return 0.0;
        }
        if(firstVideoSizeDto == null){
            return firstVideoSize;
        }
        return firstVideoSizeDto;
    }

    private Double validatedSecondVideoSizeRangeRange(ConfigurationDto configurationDto, Configuration configuration){
        Double secondVideoSizeDto = configurationDto.getSecondVideoSizeRange();
        Double secondVideoSize = configuration.getSecondVideoSizeRange();
        if(secondVideoSizeDto == null && secondVideoSize == null){
            return maxFileSize;
        }
        if(secondVideoSizeDto == null){
            return secondVideoSize;
        }
        Double firstVideoSize = configuration.getFirstVideoSizeRange();
        if(secondVideoSizeDto < firstVideoSize){
            throw new RuntimeException("Second chanel value can't be smaller than first chanel value");
        }
        return secondVideoSizeDto;
    }

    private List<Genre> validatedGenres(ConfigurationDto configurationDto, Configuration configuration){
        List<Long> genresDto = configurationDto.getGenreIds();
        List<Genre> genres = configuration.getGenres();
        System.out.println(genresDto);

        if(genresDto == null && genres == null){
            return genreRepository.findAll();
        }
        if(genresDto == null){
            return genres;
        }
        return configurationDto
                .getGenreIds()
                .stream()
                .map(id -> genreRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Codec> validatedCodecs(List<Long> codecsFromDto, MediaTypes mediaType, List<Codec> codecs){

        if(codecsFromDto == null && codecs == null){
            return codecRepository.findByMediaType(mediaType);
        }

        if(codecsFromDto == null){
            return codecs;
        }
        return codecsFromDto
                .stream()
                .map(id -> codecRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Language> validatedLanguages(ConfigurationDto configurationDto, Configuration configuration){
        List<Long> languagesDto = configurationDto.getLanguageIds();
        List<Language> languages = configuration.getAudioLanguages();
        System.out.println(languagesDto);
        if(languagesDto == null && languages == null){
            return languageRepository.findAll();
        }
        if(languagesDto == null){
            return languages;
        }
        return configurationDto
                .getLanguageIds()
                .stream()
                .map(id -> languageRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Resolution> validatedResolutions(ConfigurationDto configurationDto, Configuration configuration){
        List<Long> resolutionsDto = configurationDto.getVideoResolutionIds();
        List<Resolution> resolutions = configuration.getVideoResolutions();
        if(resolutionsDto == null && resolutions == null){
            return resolutionRepository.findAll();
        }
        if(resolutionsDto == null){
            return resolutions;
        }
        return configurationDto
                .getVideoResolutionIds()
                .stream()
                .map(id -> resolutionRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private List<AudioChannel> validatedAudioChannels(ConfigurationDto configurationDto, Configuration configuration){
        List<Long> audioChannelsDto = configurationDto.getAudioChannelIds();
        List<AudioChannel> audioChannels = configuration.getAudioChannels();
        if(audioChannelsDto == null && audioChannels == null){
            return audioChannelRepository.findAll();
        }
        if(audioChannelsDto == null){
            return audioChannels;
        }
        return configurationDto
                .getAudioChannelIds()
                .stream()
                .map(id -> audioChannelRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }
}
