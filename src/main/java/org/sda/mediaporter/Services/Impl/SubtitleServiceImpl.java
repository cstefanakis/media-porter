package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.Services.LanguageService;
import org.sda.mediaporter.Services.SubtitleService;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubtitleServiceImpl implements SubtitleService {

    private final SubtitleRepository subtitleRepository;
    private final LanguageService languageService;
    private final CodecService codecService;

    @Autowired
    public SubtitleServiceImpl(SubtitleRepository subtitleRepository, LanguageService languageService, CodecService codecService) {
        this.subtitleRepository = subtitleRepository;
        this.languageService = languageService;
        this.codecService = codecService;
    }

    @Override
    public Subtitle createSubtitle(Subtitle subtitle) {
        return subtitleRepository.save(subtitle);
    }

    @Override
    public List<Subtitle> createSubtitleListFromFile(Path file) {
        List<Subtitle> subtitlesList = new ArrayList<>();
        if(!subtitleInfo(file).isEmpty()) {
            String[] subtitles = subtitleInfo(file).split("\n");
            if(subtitles.length >= 1) {
                for (String subtitle : subtitles) {
                    String[] properties = subtitle.split(",");
                    Subtitle subtitleEntity = new Subtitle();
                    if (properties.length > 1) {
                        subtitleEntity.setFormat(codecService.autoCreateCodec(properties[1]));
                    }
                    if (properties.length > 2) {
                        subtitleEntity.setLanguage(languageService.autoCreateLanguageByCode(properties[2]));
                    }
                    subtitlesList.add(subtitleRepository.save(subtitleEntity));
                }
            }
        }return subtitlesList;
    }

    @Override
    public Subtitle updateMovieSubtitle(Long id, Subtitle subtitle, Movie movie) {
        Optional<Subtitle> subtitleEntity = subtitleRepository.findById(id);
        if (subtitleEntity.isPresent()) {
            Subtitle subtitleToUpdate = subtitleEntity.get();
            subtitleToUpdate.setMovie(movie);
            subtitleRepository.save(toEntity(subtitleToUpdate, subtitle));
        }throw new EntityNotFoundException(String.format("Subtitle with id %s not found", id));
    }

    private Subtitle toEntity(Subtitle toUpdateSubtitle, Subtitle subtitle) {
        toUpdateSubtitle.setLanguage(subtitle.getLanguage() == null? toUpdateSubtitle.getLanguage() : subtitle.getLanguage());
        toUpdateSubtitle.setFormat(subtitle.getFormat() == null? toUpdateSubtitle.getFormat() : subtitle.getFormat());
        return toUpdateSubtitle;
    }

    private String subtitleInfo(Path filePath){
        String subtitlesOptions =  FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "s",
                "-show_entries", "stream=index,codec_name:stream_tags=language",
                "-of", "csv=p=0",
                filePath.toString()
        });
        System.out.println("subtitles options: "+subtitlesOptions);
        return subtitlesOptions;
    }
}
