package org.sda.mediaporter.services.audioServices.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.sda.mediaporter.dtos.AudioDto;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.services.audioServices.AudioChannelService;
import org.sda.mediaporter.services.audioServices.AudioService;
import org.sda.mediaporter.services.CodecService;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.services.fileServices.impl.FileServiceImpl;
import org.sda.mediaporter.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;

@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;
    private final CodecService codecService;
    private final AudioChannelService audioChannelService;
    private final LanguageService languageService;

    @Autowired
    public AudioServiceImpl(AudioRepository audioRepository, CodecService codecService, AudioChannelService audioChannelService, LanguageService languageService) {
        this.audioRepository = audioRepository;
        this.codecService = codecService;
        this.languageService = languageService;
        this.audioChannelService = audioChannelService;
    }

    @Override
    @Transactional
    public List<Audio> getCreatedAudiosFromPathFile(Path filePath, VideoFilePath videoFilePath){
        String[] audiosLines = audioInfo(filePath).split("\\R");
        List<Audio> audios = new ArrayList<>();
        for (String ffMpegProperties : audiosLines) {
            Audio audio = generatedAudiosFromFFMpeg(ffMpegProperties);
            audio.setVideoFilePath(videoFilePath);
            audios.add(audioRepository.save(audio));
        }
        videoFilePath.setAudios(audios);
        return audios;
    }

    @Override
    public List<Audio> getAudiosFromPathFile(Path filePath, VideoFilePath videoFilePath){
        String[] audiosLines = audioInfo(filePath).split("\\R");
        List<Audio> audios = new ArrayList<>();
        for (String ffMpegProperties : audiosLines) {
            Audio audio = generatedAudiosFromFFMpeg(ffMpegProperties);
            audio.setVideoFilePath(videoFilePath);
            audios.add(audio);
        }
        return audios;
    }

    @Override
    @Transactional
    public void deleteAudioById(Long audioId) {
        audioRepository.deleteById(audioId);
    }

    @Override
    public List<AudioDto> getAudiosDetails(Path file) {
        String[] audiosLines = audioInfo(file).split("\\R");
        List<AudioDto> audios = new ArrayList<>();
        for (String ffMpegProperties : audiosLines) {
            AudioDto audioDto = getAudioDto(ffMpegProperties);
            audios.add(audioDto);
        }
        return audios;
    }

    private AudioDto getAudioDto(String ffMpegProperties){
        String[] properties = ffMpegProperties.split(",", -1);

        String codecName = getValidatedStringProperty(properties[0]);
        Integer audioChannels = properties.length > 1 ? getValidatedIntegerProperty(properties[1]) : null;
        Integer audioBitrate = properties.length > 2 ? getValidatedIntegerProperty(properties[2]) : null;
        String languageCode = properties.length > 3 ? getValidatedStringProperty(properties[3]) : null;

        return AudioDto.builder()
                .audioCodec(codecName)
                .audioChannel(audioChannels)
                .audioBitrate(audioBitrate)
                .audioLanguage(languageCode)
                .build();
    }

    private Audio generatedAudiosFromFFMpeg(String ffMpegProperties){
        AudioDto audioDto = getAudioDto(ffMpegProperties);
        Integer audioChannel = audioDto.getAudioChannel();
        String audioCodec = audioDto.getAudioCodec();
        Integer audioBitrate = audioDto.getAudioBitrate();
        String audioLanguage = audioDto.getAudioLanguage();
        return Audio.builder()
                .audioChannel(audioChannel == null
                        ? null
                        : audioChannelService.getAudioChannelByChannelsOrNull(audioChannel))
                .codec(audioCodec == null
                        ? null
                        : codecService.getOrCreateCodecByCodecNameAndMediaType(audioCodec, MediaTypes.AUDIO))
                .bitrate(audioBitrate)
                .language(audioLanguage == null
                        ? null
                        : getLanguageByCodeTitleOrNull(audioLanguage))
                .build();
    }

    private Language getLanguageByCodeTitleOrNull(String languageCode){
        try{
            return languageService.getLanguageByCodeOrTitle(languageCode);
        }catch (EntityNotFoundException e){
            return null;
        }
    }

    private String getValidatedStringProperty(String property){
        if(property.isEmpty()
                || property.equalsIgnoreCase("N/A")
                || property.equalsIgnoreCase("und")){
            return null;
        }
        String capitalizeFirstCharacterAndMakeTheRestLowercase =  (property.substring(0, 1).toUpperCase() + property.substring(1).toLowerCase());
        return capitalizeFirstCharacterAndMakeTheRestLowercase.trim();
    }

    private Integer getValidatedIntegerProperty(String property){
        String validatedStringProperty = getValidatedStringProperty(property);
        return validatedStringProperty == null
                ? null
                : Integer.parseInt(validatedStringProperty);
    }

    //Output aac,2,128000,eng
    private String audioInfo(Path filePath){
        return  FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "a",
                "-show_entries", "stream=codec_name,channels,bit_rate:stream_tags=language",
                "-of", "csv=p=0",
                filePath.toString()
        });
    }
}
