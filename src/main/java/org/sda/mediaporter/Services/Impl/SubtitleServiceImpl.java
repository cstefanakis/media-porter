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
    public List<Subtitle> createSubtitleListFromFile(Path file, Movie movie) {
        List<Subtitle> subtitlesList = new ArrayList<>();
        if(!subtitleInfo(file).isEmpty()) {
            String[] subtitles = subtitleInfo(file).split("\n");

            for (String subtitle : subtitles) {
                //Output String subrip,eng
                String[] properties = subtitle.split(",",-1);
                String subtitleCodecName = properties[0].isEmpty() || properties[0].equals("N/A")? null : properties[0].replaceAll("[^a-zA-Z0-9]", "");
                String languageCodec = null;
                try {
                    languageCodec = properties[1].isEmpty() || properties[1].equals("N/A") || properties[1].trim().equals("und")? null : properties[1].replaceAll("[^a-zA-Z0-9]", "");
                } catch (Exception ignored) {
                }
                Codec subtitleCodec = subtitleCodecName == null? null : codecService.getCodecByNameAndMediaType(subtitleCodecName, MediaTypes.SUBTITLE);
                Language language = languageCodec == null? null : languageService.getLanguageByCode(languageCodec);
                subtitlesList.add(subtitleRepository.save(Subtitle.builder()
                                .language(language)
                                .format(subtitleCodec)
                                .movie(movie)
                        .build()));
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
