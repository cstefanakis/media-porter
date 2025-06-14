package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.AudioService;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Audio;
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

    @Autowired
    public AudioServiceImpl(AudioRepository audioRepository, CodecService codecService, LanguageService languageService) {
        this.audioRepository = audioRepository;
        this.codecService = codecService;
        this.languageService = languageService;
    }

    @Override
    public Audio createAudio(Audio audio) {
        return audioRepository.save(audio);
    }

    @Override
    public List<Audio> createAudioListFromFile(Path file) {
        List<Audio> audios = new ArrayList<>();
        String audioInfo = audioInfo(file);
        if(!audioInfo.isEmpty()) {
        String[] audioInfoArray = audioInfo.split("\n");
        for(String audio : audioInfoArray) {
            String[] audioItems = audio.split(",");
            Audio newAudio = new Audio();
            System.out.println("audio length: "+audioItems.length);
            if(audioItems.length > 1) {
                newAudio.setCodec(codecService.autoCreateCodec(audioItems[1]));
            }
            if(audioItems.length > 2) {
                newAudio.setChannels(Integer.parseInt(audioItems[2]));
            }
            if(audioItems.length > 3) {
                newAudio.setBitrate(FileServiceImpl.convertStringToInt(audioItems[3]));
            }
            if(audioItems.length > 4) {
                newAudio.setLanguage(languageService.autoCreateLanguageByCode(audioItems[4]));
            }
            audios.add(audioRepository.save(newAudio));
        }
        }return audios;
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
        toUpdateAudio.setChannels(audio.getChannels() == null? toUpdateAudio.getChannels(): audio.getChannels());
        toUpdateAudio.setBitrate(audio.getBitrate() == null? toUpdateAudio.getBitrate(): audio.getBitrate());
        return toUpdateAudio;
    }

    private String audioInfo(Path filePath){
        return  FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "a",
                "-show_entries", "stream=index,codec_name,channels,bit_rate:stream_tags=language",
                "-of", "csv=p=0",
                filePath.toString()
        });
    }


}
