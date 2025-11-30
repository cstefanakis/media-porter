package org.sda.mediaporter.services.audioServices.impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.models.Language;
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

    public List<Audio> getCreatedAudiosFromPathFile(Path filePath){
        List<Audio> pathFileAudios = getAudiosFromPathFile(filePath);
        return pathFileAudios.stream()
                .map(audioRepository::save)
                .toList();
    }

    public List<Audio> getAudiosFromPathFile(Path filePath){
        List<Audio> audios = new ArrayList<>();
        String[] audiosLines = audioInfo(filePath).split("\\R");
        for (String ffMpegProperties : audiosLines) {
            Audio audio = generatedAudiosFromFFMpeg(ffMpegProperties);
            audios.add(audio);
        }
        return audios;
    }

    private Audio generatedAudiosFromFFMpeg(String ffMpegProperties){
        String[] properties = ffMpegProperties.split(",", -1);

        String codecName = getValidatedStringProperty(properties[0]);
        Integer audioChannels = getValidatedIntegerProperty(properties[1]);
        Integer audioBitrate = getValidatedIntegerProperty(properties[2]);
        String languageCode = getValidatedStringProperty(properties[3]);

        return Audio.builder()
                .audioChannel(audioChannels == null
                        ? null
                        : audioChannelService.getAudioChannelByChannelsOrNull(audioChannels))
                .codec(codecName == null
                        ? null
                        : codecService.getOrCreateCodecByCodecNameAndMediaType(codecName, MediaTypes.AUDIO))
                .bitrate(audioBitrate)
                .language(languageCode == null
                        ? null
                        : getLanguageByCodeTitleOrNull(languageCode))
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
