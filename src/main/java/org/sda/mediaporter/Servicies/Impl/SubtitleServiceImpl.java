package org.sda.mediaporter.Servicies.Impl;

import org.sda.mediaporter.Servicies.CodecService;
import org.sda.mediaporter.Servicies.LanguageService;
import org.sda.mediaporter.Servicies.SubtitleService;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        String[] subtitles = subtitleInfo(file).split("\n");
        if(subtitles.length >= 1) {
            for (String subtitle : subtitles) {
                String[] properties = subtitle.split(",");

                if(properties.length >= 3) {
                    subtitlesList.add(Subtitle.builder()
                            .language(languageService.autoCreateLanguageByCode(properties[2]))
                            .format(codecService.autoCreateCodec(properties[1]))
                            .build());
                }
            }
        }return subtitlesList;
    }

    private String subtitleInfo(Path filePath){
        return FileServiceImpl.runCommand(new String[]{
                "ffprobe",
                "-v", "error",
                "-select_streams", "s",
                "-show_entries", "stream=index,codec_name:stream_tags=language",
                "-of", "csv=p=0",
                filePath.toString()
        });
    }
}
