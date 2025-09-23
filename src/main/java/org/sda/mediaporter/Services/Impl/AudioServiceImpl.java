package org.sda.mediaporter.Services.Impl;

import org.sda.mediaporter.Services.AudioChannelService;
import org.sda.mediaporter.Services.AudioService;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.dtos.AudioChannelDto;
import org.sda.mediaporter.dtos.CodecDto;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Parser;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;
    private final CodecService codecService;
    private final CodecRepository codecRepository;
    private final LanguageRepository languageRepository;
    private final AudioChannelService audioChannelService;
    private final AudioChannelRepository audioChannelRepository;

    @Autowired
    public AudioServiceImpl(AudioRepository audioRepository, CodecService codecService, CodecRepository codecRepository, LanguageRepository languageRepository, AudioChannelService audioChannelService, AudioChannelRepository audioChannelRepository) {
        this.audioRepository = audioRepository;
        this.codecService = codecService;
        this.codecRepository = codecRepository;
        this.languageRepository = languageRepository;
        this.audioChannelService = audioChannelService;
        this.audioChannelRepository = audioChannelRepository;
    }

    @Override
    public List<Audio> createAudioListFromFile(Path file, Movie movie) {
        return getAudioListFromFile(file).stream()
                .peek(audio -> audio.setMovie(movie))
                .peek(audio -> audioRepository.save(audio))
                .toList();
    }

    @Override
    public List<Audio> getAudioListFromFile(Path file) {
        List<Audio> audios = new ArrayList<>();
        String audioInfo = audioInfo(file);
        if(!audioInfo.isEmpty()) {
            //Create array for all audios
            String[] audioInfoArray = audioInfo.split("\n");
            for(String audio : audioInfoArray) {
                //Create array for all properties in audio
                audios.add((Audio.builder()
                        .codec(getAudioCodec(properties(audio)[0]))
                        .bitrate(getAudioBitrate(properties(audio)[2]))
                        .audioChannel(getAudioChannel(properties(audio)[1]))
                        .language(getAudioLanguageByCode(properties(audio)[3]))
                        .build()));
            }
        }return audios;
    }

    public String[] properties(String audio){
        String[] fileInfo =  audio.split(",", -1);
        String[] properties = new String[4];
        for(int i = 0 ; i < properties.length ; i++){
            if(i < fileInfo.length){
                properties[i] = fileInfo[i];
            }else{
                properties[i] = null;
            }
        }
        return properties;
    }

    @Override
    public Codec getAudioCodec(String codecAudioProperty) {
        if(codecAudioProperty == null){
            return null;
        }

        codecAudioProperty = codecAudioProperty.trim();
        if(codecAudioProperty.equals("N/A")){
            return null;
        }

        codecAudioProperty = codecAudioProperty.replaceAll("[^a-zA-Z0-9]", "");
        if(codecAudioProperty.isBlank()){
            return null;
        }

        String finalAudioCodec = codecAudioProperty;
        return codecRepository.findByNameAndMediaType(codecAudioProperty, MediaTypes.AUDIO)
                .orElseGet(() -> codecService.autoCreateCodec(finalAudioCodec, MediaTypes.AUDIO));
    }

    @Override
    public AudioChannel getAudioChannel(String chanelAudioProperty) {
        if(chanelAudioProperty == null){
            return null;
        }

        chanelAudioProperty = chanelAudioProperty.trim();
        if(chanelAudioProperty.equals("N/A")){
            return null;
        }

        chanelAudioProperty = chanelAudioProperty.replaceAll("[^a-zA-Z0-9]", "");
        if(chanelAudioProperty.isBlank()){
            return null;
        }

        int audioChannelInt = Integer.parseInt(chanelAudioProperty);
        return audioChannelRepository.findAudioChannelByChannel(audioChannelInt).orElseGet(
                () -> audioChannelService.createAudioChannel(AudioChannelDto.builder()
                                .title(String.format("%s Channels Sound", audioChannelInt))
                                .channels(audioChannelInt)
                        .build()));
    }

    @Override
    public Integer getAudioBitrate(String bitrateAudioProperty) {
        if(bitrateAudioProperty == null){
            return null;
        }

        return bitrateAudioProperty.isEmpty()
                || bitrateAudioProperty.trim().equals("N/A")
                ? null
                : Integer.parseInt(bitrateAudioProperty.replaceAll("[^a-zA-Z0-9]", "")) / 1000;
    }

    @Override
    public Language getAudioLanguageByCode(String codeLanguageAudioProperty) {
        if(codeLanguageAudioProperty == null){
            return null;
        }

        codeLanguageAudioProperty = codeLanguageAudioProperty.trim();

        if(codeLanguageAudioProperty.isEmpty()
                || codeLanguageAudioProperty.equals("N/A")
                || codeLanguageAudioProperty.equals("und")){
            return null;
        }

        Optional<Language> languageOptional =  languageRepository.findByCodeOrTitle(codeLanguageAudioProperty);
        return languageOptional.orElse(null);
    }

    //Output aac,2,128000,eng
    public String audioInfo(Path filePath){
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
