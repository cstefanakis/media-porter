package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.Services.SubtitleService;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubtitleServiceImpl implements SubtitleService {

    private final CodecRepository codecRepository;
    private final LanguageRepository languageRepository;
    private final CodecService codecService;

    @Autowired
    public SubtitleServiceImpl(CodecRepository codecRepository, LanguageRepository languageRepository, CodecService codecService) {
        this.codecRepository = codecRepository;
        this.languageRepository = languageRepository;
        this.codecService = codecService;
    }

    @Override
    public List<Subtitle> createSubtitleListFromFile(Path file, Movie movie) {
        List<Subtitle> subtitles = new ArrayList<>();
        if(!subtitleInfo(file).isEmpty()) {
            String[] subtitlesInfo = subtitleInfo(file).split("\n");

            for (String subtitle : subtitlesInfo) {
                subtitles.add(Subtitle.builder()
                                .format(getSubtitleCodec(subtitlesProperties(subtitle)))
                                .language(getLanguage(subtitlesProperties(subtitle)))
                        .build());
            }

        }return subtitles;
    }

    private String[] subtitlesProperties(String subtitle){
        String[] fileInfo =  subtitle.split(",", -1);
        String[] properties = new String[2];
        for(int i = 0 ; i < properties.length ; i++){
            if(i < fileInfo.length){
                properties[i] = fileInfo[i];
            }else{
                properties[i] = null;
            }
        }
        return properties;
    }

    private Codec getSubtitleCodec(String[] subtitlesProperties) {
        String subtitleCodec = subtitlesProperties[0];
        if(subtitleCodec == null){
            return null;
        }

        subtitleCodec = subtitleCodec.trim();
        if(subtitleCodec.equalsIgnoreCase("N/A")){
            return null;
        }

        subtitleCodec = subtitleCodec.replaceAll("[^a-zA-Z0-9]", "");
        if(subtitleCodec.isBlank()){
            return null;
        }

        String finalAudioCodec = subtitleCodec;
        return codecRepository.findByNameAndMediaType(subtitleCodec, MediaTypes.SUBTITLE)
                .orElseGet(() -> codecService.autoCreateCodec(finalAudioCodec, MediaTypes.SUBTITLE));
    }

    private Language getLanguage(String[] subtitlesProperties){
        String language = subtitlesProperties[1];
        if(language == null){
            return null;
        }

        language = language.trim();
        if(language.isEmpty()
                || language.equalsIgnoreCase("N/A")
                || language.equalsIgnoreCase("und")){
            return null;
        }

        return languageRepository.findByCodeOrTitle(language).orElse(null);
    }

    //Output String subrip,eng
    private String subtitleInfo(Path filePath){
        String subtitlesOptions =  FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "s",
                "-show_entries", "stream=codec_name:stream_tags=language",
                "-of", "csv=p=0",
                filePath.toString()
        });
        System.out.println("subtitles options: "+subtitlesOptions);
        return subtitlesOptions;
    }
}
