package org.sda.mediaporter.Services.Impl;

import org.sda.mediaporter.Services.AudioChannelService;
import org.sda.mediaporter.Services.AudioService;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;
    private final CodecService codecService;
    private final LanguageService languageService;
    private final AudioChannelService audioChannelService;

    @Autowired
    public AudioServiceImpl(AudioRepository audioRepository, CodecService codecService, LanguageService languageService, AudioChannelService audioChannelService) {
        this.audioRepository = audioRepository;
        this.codecService = codecService;
        this.languageService = languageService;
        this.audioChannelService = audioChannelService;
    }

    @Override
    public List<Audio> createAudioListFromFile(Path file, Movie movie) {
        List<Audio> audiosFile = getAudioListFromFile(file);
        for(Audio audio : audiosFile){
            audio.setMovie(movie);
            audioRepository.save(audio);
        }
        return audiosFile;
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
                String[] properties = audio.split(",", -1);
                audios.add((Audio.builder()
                        .codec(getAudioCodec(properties))
                        .bitrate(getAudioBitrate(properties))
                        .audioChannel(getAudioChannel(properties))
                        .language(getAudioLanguageByCode(properties))
                        .build()));
            }
        }return audios;
    }

    @Override
    public Codec getAudioCodec(String[] audioProperties) {
        String audioCodec = audioProperties[0].isEmpty() || audioProperties[0].trim().equals("N/A") ? null : audioProperties[0].replaceAll("[^a-zA-Z0-9]", "");
        return audioCodec == null? null : codecService.getCodecByNameAndMediaType(audioCodec, MediaTypes.AUDIO);
    }

    @Override
    public AudioChannel getAudioChannel(String[] audioProperties) {
        Integer channel = audioProperties[1].isEmpty() || audioProperties[1].trim().equals("N/A")? null : Integer.parseInt(audioProperties[1].replaceAll("[^a-zA-Z0-9]", ""));
        return audioChannelService.getAudioChannelByChannels(channel);
    }

    @Override
    public Integer getAudioBitrate(String[] audioProperties) {
        return audioProperties[2].isEmpty() || audioProperties[2].trim().equals("N/A")? null : Integer.parseInt(audioProperties[2].replaceAll("[^a-zA-Z0-9]", "")) / 1000;
    }

    @Override
    public Language getAudioLanguageByCode(String[] audioProperties) {
        try {
            String language =  audioProperties[3].isEmpty() || audioProperties[3].trim().equals("N/A") || audioProperties[3].trim().equals("und") ? null : audioProperties[3].replaceAll("[^a-zA-Z0-9]", "");
            if(language == null){
                return null;
            }
            return languageService.getLanguageByCode(language);
        } catch (Exception ignored) {

        }return null;
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
