package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.ConfigurationService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Validated
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final GenreRepository genreRepository;
    private final CodecRepository codecRepository;
    private final LanguageRepository languageRepository;
    private final ResolutionRepository resolutionRepository;
    private final AudioChannelRepository audioChannelRepository;


    private double maxFileSize = 31457280;
    private int maxVideoBitrate = 200000000;
    private int maxAudioBitrate = 2048000;
    private int maxDaysToPast = 9000;

    @Autowired
    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository, GenreRepository genreRepository, CodecRepository codecRepository, LanguageRepository languageRepository, ResolutionRepository resolutionRepository, AudioChannelRepository audioChannelRepository) {
        this.configurationRepository = configurationRepository;
        this.genreRepository = genreRepository;
        this.codecRepository = codecRepository;
        this.languageRepository = languageRepository;
        this.resolutionRepository = resolutionRepository;
        this.audioChannelRepository = audioChannelRepository;
    }

    @Override
    public Configuration getConfiguration() {
        return  configurationRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Configuration with id: 1 not exist"));
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
    public void updateConfiguration(ConfigurationDto configurationDto) {
        System.out.println(configurationDto.getMaxDatesControlFilesFromExternalSource());
        Configuration configuration = toEntity(configurationDto);
        configurationRepository.save(configuration);
    }


    private Configuration toEntity(ConfigurationDto configurationDto) {

        Configuration config = getConfiguration();
        config.setMaxDatesSaveFile(configurationDto.getMaxDatesSaveFile() == null? this.maxDaysToPast: configurationDto.getMaxDatesSaveFile());
        config.setMaxDatesControlFilesFromExternalSource(configurationDto.getMaxDatesControlFilesFromExternalSource() == null? 0: configurationDto.getMaxDatesControlFilesFromExternalSource());

        config.setVideoResolutions(validatedResolutions(configurationDto, config));

        config.setFirstVideoBitrateValueRange(validatedFirstVideoBitrateValueRange(configurationDto, config));
        config.setSecondVideoBitrateValueRange(validatedSecondVideoBitrateValueRange(configurationDto, config));

        config.setVideoCodecs(validatedCodecs(configurationDto.getVideoCodecs(), MediaTypes.VIDEO, config.getVideoCodecs()));

        config.setFirstAudioBitrateValueRange(validatedFirstAudioBitrateValueRange(configurationDto, config));
        config.setSecondAudioBitrateValueRange(validatedSecondAudioBitrateValueRange(configurationDto, config));

        config.setAudioChannels(validatedAudioChannels(configurationDto, config));

        config.setAudioCodecs(validatedCodecs(configurationDto.getAudioCodecs(), MediaTypes.AUDIO, config.getAudioCodecs()));

        config.setGenres(validatedGenres(configurationDto, config));

        config.setAudioLanguages(configurationDto.getLanguages());

        config.setFirstVideoSizeRange(validatedFirstVideoSizeRangeRange(configurationDto, config));
        config.setSecondVideoSizeRange(validatedSecondVideoSizeRangeRange(configurationDto, config));

        return config;
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
        List<Genre> genresDto = configurationDto.getGenres();
        List<Genre> genres = configuration.getGenres();

        if(genresDto == null && genres == null){
            return genreRepository.findAll();
        }
        if(genresDto == null){
            return genres;
        }
        return new ArrayList<>(new HashSet<>(genresDto));
    }

    private List<Codec> validatedCodecs(List<Codec> codecsFromDto, MediaTypes mediaType, List<Codec> codecs){

        if(codecsFromDto == null && codecs == null){
            return codecRepository.findByMediaType(mediaType);
        }

        if(codecsFromDto == null){
            return codecs;
        }
        return new ArrayList<>(new HashSet<>(codecsFromDto));
    }

    private List<Language> validatedLanguages(ConfigurationDto configurationDto, Configuration configuration){
        List<Language> languagesDto = configurationDto.getLanguages();
        List<Language> languages = configuration.getAudioLanguages();
        if(languagesDto == null && languages == null){
            return languageRepository.findAll();
        }
        if(languagesDto == null){
            return languages;
        }
        return new ArrayList<>(new HashSet<>(languagesDto));
    }

    private List<Resolution> validatedResolutions(ConfigurationDto configurationDto, Configuration configuration){
        List<Resolution> resolutionsDto = configurationDto.getVideoResolutions();
        List<Resolution> resolutions = configuration.getVideoResolutions();
        if(resolutionsDto == null && resolutions == null){
            return resolutionRepository.findAll();
        }
        if(resolutionsDto == null){
            return resolutions;
        }
        return new ArrayList<>(new HashSet<>(resolutionsDto));
    }

    private List<AudioChannel> validatedAudioChannels(ConfigurationDto configurationDto, Configuration configuration){
        List<AudioChannel> audioChannelsDto = configurationDto.getAudioChannels();
        List<AudioChannel> audioChannels = configuration.getAudioChannels();
        if(audioChannelsDto == null && audioChannels == null){
            return audioChannelRepository.findAll();
        }
        if(audioChannelsDto == null){
            return audioChannels;
        }
        return new ArrayList<>(new HashSet<>(audioChannelsDto));
    }
}
