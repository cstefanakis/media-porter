package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;

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
    public Audio createAudio(Audio audio) {
        return audioRepository.save(audio);
    }

    @Override
    public List<Audio> createAudioListFromFile(Path file, Movie movie) {
        List<Audio> audios = new ArrayList<>();
        String audioInfo = audioInfo(file);
        if(!audioInfo.isEmpty()) {
            //Create array for all audios
            String[] audioInfoArray = audioInfo.split("\n");
            for(String audio : audioInfoArray) {
                //Create array for all properties in audio
                String[] properties = audio.split(",", -1);
                String audioCodecName = properties[0].isEmpty() || properties[0].trim().equals("N/A") ? null : properties[0].replaceAll("[^a-zA-Z0-9]", "");
                Integer audioChannels = properties[1].isEmpty() || properties[1].trim().equals("N/A")? null : Integer.parseInt(properties[1].replaceAll("[^a-zA-Z0-9]", ""));
                Integer audioBitrateKbps = properties[2].isEmpty() || properties[2].trim().equals("N/A")? null : Integer.parseInt(properties[2].replaceAll("[^a-zA-Z0-9]", "")) / 1000;
                String audioLanguageCode = null;
                try {
                    audioLanguageCode = properties[3].isEmpty() || properties[3].trim().equals("N/A") || properties[3].trim().equals("und") ? null : properties[3].replaceAll("[^a-zA-Z0-9]", "");
                } catch (Exception e) {
                }
                Codec audioCodec = audioCodecName == null? null : codecService.getCodecByNameAndMediaType(audioCodecName, MediaTypes.AUDIO);

                Language audioLanguage = validatedLanguage(audioLanguageCode);
                AudioChannel audioChannel = audioChannelService.getAudioChannelByChannels(audioChannels);
                audios.add(audioRepository.save(Audio.builder()
                                .codec(audioCodec)
                                .bitrate(audioBitrateKbps)
                                .audioChannel(audioChannel)
                                .language(audioLanguage)
                                .movie(movie)
                        .build()));
            }
        }return audios;
    }

    private Language validatedLanguage(String language){
        if(language == null){
            return null;
        }
        if(language.length() > 3){
            return languageService.getLanguageByTitle(language);
        }
        return languageService.getLanguageByCode(language);
    }

    @Override
    public Audio updateMovieAudio(Long id, Audio audio, Movie movie) {
        Optional<Audio> audioOptional = audioRepository.findById(id);
        if(audioOptional.isPresent()) {
            Audio audioToUpdate = audioOptional.get();
            audioToUpdate.setMovie(movie);
            return audioRepository.save(toEntity(audioToUpdate, audio));
        }throw new EntityNotFoundException(String.format("Audio with id %s not found", id));
    }

    private Audio toEntity(Audio toUpdateAudio, Audio audio) {
        toUpdateAudio.setCodec(audio.getCodec() == null? toUpdateAudio.getCodec(): audio.getCodec());
        toUpdateAudio.setLanguage(audio.getLanguage() == null? toUpdateAudio.getLanguage(): audio.getLanguage());
        toUpdateAudio.setAudioChannel(audio.getAudioChannel() == null? toUpdateAudio.getAudioChannel(): audio.getAudioChannel());
        toUpdateAudio.setBitrate(audio.getBitrate() == null? toUpdateAudio.getBitrate(): audio.getBitrate());
        return toUpdateAudio;
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
