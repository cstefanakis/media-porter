package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.ConfigurationService;
import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.dtos.GenreResponseDto;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.GenreRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final GenreRepository genreRepository;
    private final CodecRepository codecRepository;
    private final LanguageRepository languageRepository;


    private double maxFileSize = 31457280;
    private int maxVideoBitrate = 200000000;
    private int maxAudioBitrate = 2048000;
    private int maxAudioChannels = 24;
    private int maxVideoResolution = 15360;
    private int maxDaysToPast = 9000;

    @Autowired
    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository, GenreRepository genreRepository, CodecRepository codecRepository, LanguageRepository languageRepository) {
        this.configurationRepository = configurationRepository;
        this.genreRepository = genreRepository;
        this.codecRepository = codecRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    public Configuration getConfiguration() {
        return  configurationRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Configuration with id: 1 not exist"));
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

        config.setFirstVideoResolutionValueRange(validatedFirstVideoResolutionValueRange(configurationDto, config));
        config.setSecondVideoResolutionValueRange(validatedSecondVideoResolutionValueRange(configurationDto, config));

        config.setFirstVideoBitrateValueRange(validatedFirstVideoBitrateValueRange(configurationDto, config));
        config.setSecondVideoBitrateValueRange(validatedSecondVideoBitrateValueRange(configurationDto, config));

        config.setVideoCodecsPrefer(validatedCodecs(configurationDto.getVideoCodecsPrefer(), MediaTypes.VIDEO));

        config.setFirstAudioBitrateValueRange(validatedFirstAudioBitrateValueRange(configurationDto, config));
        config.setSecondAudioBitrateValueRange(validatedSecondAudioBitrateValueRange(configurationDto, config));

        config.setFirstAudioChannelsValueRange(validatedFirstAudioChannelsValueRange(configurationDto, config));
        config.setSecondAudioChannelsValueRange(validatedSecondAudioChannelsValueRange(configurationDto, config));

        config.setAudioCodecsPrefer(validatedCodecs(configurationDto.getAudioCodecsPrefer(), MediaTypes.AUDIO));

        config.setGenresPrefer(validatedGenres(configurationDto, config));

        config.setFirstVideoSizeRangeRange(validatedFirstVideoSizeRangeRange(configurationDto, config));
        config.setSecondVideoSizeRangeRange(validatedSecondVideoSizeRangeRange(configurationDto, config));

        return config;
    }

    private Integer validatedFirstVideoResolutionValueRange(ConfigurationDto configurationDto, Configuration config){
        int value = 0;
        if(configurationDto.getFirstVideoResolutionValueRange() != null){
            value = configurationDto.getFirstVideoResolutionValueRange();
        }
        if(config.getFirstVideoResolutionValueRange() != null && configurationDto.getFirstVideoResolutionValueRange() == null){
            value = config.getFirstVideoResolutionValueRange();
        }

        return value;
    }

    private Integer validatedSecondVideoResolutionValueRange(ConfigurationDto configurationDto, Configuration config){

        if(configurationDto.getSecondVideoResolutionValueRange() != null){
            this.maxVideoResolution = configurationDto.getSecondVideoResolutionValueRange();
        }
        if(config.getSecondVideoResolutionValueRange() != null && configurationDto.getSecondVideoResolutionValueRange() == null) {
            this.maxVideoResolution = config.getSecondVideoResolutionValueRange();
        }
        if(this.maxVideoResolution < config.getFirstVideoResolutionValueRange()){
            throw new RuntimeException("Second resolution value can't be smaller than first resolution value");
        }
        return this.maxVideoResolution;
    }

    private Integer validatedFirstVideoBitrateValueRange(ConfigurationDto configurationDto, Configuration config){
        int value = 0;
        if(configurationDto.getFirstVideoBitrateValueRange() != null){
            value = configurationDto.getFirstVideoBitrateValueRange();
        }
        if(config.getFirstVideoBitrateValueRange() != null && configurationDto.getFirstVideoBitrateValueRange() == null){
            value = config.getFirstVideoBitrateValueRange();
        }

        return value;
    }

    private Integer validatedSecondVideoBitrateValueRange(ConfigurationDto configurationDto, Configuration config){
        if(configurationDto.getSecondVideoBitrateValueRange() != null){
            this.maxVideoBitrate = configurationDto.getSecondVideoBitrateValueRange();
        }
        if(config.getSecondVideoBitrateValueRange() != null && configurationDto.getSecondVideoBitrateValueRange() == null) {
            this.maxVideoBitrate = config.getSecondVideoBitrateValueRange();
        }
        if(this.maxVideoBitrate < config.getFirstVideoBitrateValueRange()){
            throw new RuntimeException("Second bitrate value can't be smaller than first bitrate value");
        }
        return this.maxVideoBitrate;
    }

    private Integer validatedFirstAudioBitrateValueRange(ConfigurationDto configurationDto, Configuration config){
        int value = 0;
        if(configurationDto.getFirstAudioBitrateValueRange() != null){
            value = configurationDto.getFirstAudioBitrateValueRange();
        }
        if(config.getFirstAudioBitrateValueRange() != null && configurationDto.getFirstAudioBitrateValueRange() == null){
            value = config.getFirstAudioBitrateValueRange();
        }

        return value;
    }

    private Integer validatedSecondAudioBitrateValueRange(ConfigurationDto configurationDto, Configuration config){

        if(configurationDto.getSecondAudioBitrateValueRange() != null){
            this.maxAudioBitrate = configurationDto.getSecondAudioBitrateValueRange();
        }
        if(config.getSecondAudioBitrateValueRange() != null && configurationDto.getSecondAudioBitrateValueRange() == null) {
            this.maxAudioBitrate = config.getSecondAudioBitrateValueRange();
        }
        if(this.maxAudioBitrate < config.getFirstAudioBitrateValueRange()){
            throw new RuntimeException("Second bitrate value can't be smaller than first bitrate value");
        }
        return this.maxAudioBitrate;
    }

    private Integer validatedFirstAudioChannelsValueRange(ConfigurationDto configurationDto, Configuration config){
        int value = 0;
        if(configurationDto.getFirstAudioChannelsValueRange() != null){
            value = configurationDto.getFirstAudioChannelsValueRange();
        }
        if(config.getFirstAudioChannelsValueRange() != null && configurationDto.getFirstAudioChannelsValueRange() == null){
            value = config.getFirstAudioChannelsValueRange();
        }

        return value;
    }

    private Integer validatedSecondAudioChannelsValueRange(ConfigurationDto configurationDto, Configuration config){
        if(configurationDto.getSecondAudioChannelsValueRange() != null){
            this.maxAudioChannels = configurationDto.getSecondAudioChannelsValueRange();
        }
        if(config.getSecondAudioChannelsValueRange() != null && configurationDto.getSecondAudioChannelsValueRange() == null) {
            this.maxAudioChannels = config.getSecondAudioChannelsValueRange();
        }
        if(this.maxAudioChannels < config.getFirstAudioChannelsValueRange()){
            throw new RuntimeException("Second chanel value can't be smaller than first chanel value");
        }
        return this.maxAudioChannels;
    }

    private Double validatedFirstVideoSizeRangeRange(ConfigurationDto configurationDto, Configuration config){
        double value = 0;
        if(configurationDto.getFirstVideoSizeRangeRange() != null){
            value = configurationDto.getFirstVideoSizeRangeRange();
        }
        if(config.getFirstVideoSizeRangeRange() != null && configurationDto.getFirstVideoSizeRangeRange() == null){
            value = config.getFirstVideoSizeRangeRange();
        }

        return value;
    }

    private Double validatedSecondVideoSizeRangeRange(ConfigurationDto configurationDto, Configuration config){

        if(configurationDto.getSecondVideoSizeRangeRange() != null){
            this.maxFileSize = configurationDto.getSecondVideoSizeRangeRange();
        }
        if(config.getSecondVideoSizeRangeRange() != null && configurationDto.getSecondVideoSizeRangeRange() == null) {
            this.maxFileSize = config.getSecondVideoSizeRangeRange();
        }
        if(this.maxFileSize < config.getFirstVideoSizeRangeRange()){
            throw new RuntimeException("Second chanel value can't be smaller than first chanel value");
        }
        return this.maxFileSize;
    }

    private List<Genre> validatedGenres(ConfigurationDto configurationDto, Configuration configuration){
        List<Genre> genres = new ArrayList<>();
        List<String> codecsFromDto = configurationDto.getGenresPrefer();
        if(!codecsFromDto.isEmpty()){
            for(String genre : codecsFromDto){
                Genre genreFromDb = genreRepository.findGenreByTitle(genre).orElseThrow(
                        ()-> new EntityNotFoundException(String.format("Genre with name: %s not found", genre)));
                genres.add(genreFromDb);
            }
        }
        return genres;
    }

    private List<Codec> validatedCodecs(List<String> videoCodecsFromDto, MediaTypes mediaType){
        List<Codec> codecs = new ArrayList<>();
        if(!videoCodecsFromDto.isEmpty()) {
            for (String codec : videoCodecsFromDto) {
                Codec codecFromDb = codecRepository.findByNameAndMediaType(codec, mediaType).orElseThrow(
                        () -> new EntityNotFoundException(String.format("Codec with name: %s not found", codec)));
                codecs.add(codecFromDb);
            }
        }return codecs;
    }

}
