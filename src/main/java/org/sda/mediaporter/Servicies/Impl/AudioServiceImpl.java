package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.AudioService;
import org.sda.mediaporter.Servicies.CodecService;
import org.sda.mediaporter.Servicies.FileService;
import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.models.Audio;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.repositories.AudioRepository;
import org.sda.mediaporter.repositories.CodecRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AudioServiceImpl implements AudioService {

    private final AudioRepository audioRepository;
    private final CodecService codecService;
    private final LanguageService languageService;

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
        String[] audioInfoArray = audioInfo.split("\n");
        for(String audio : audioInfoArray) {
            String[] audioItems = audio.split(",");
             audios.add(createAudio(Audio.builder()
                    .codec(codecService.autoCreateCodec(audioItems[1]))
                    .channels(Integer.parseInt(audioItems[2]))
                    .bitrate(FileServiceImpl.convertStringToInt(audioItems[3]))
                    .language(languageService.autoCreateLanguageByCode(audioItems[4]))
                    .build()));
        }return audios;
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
