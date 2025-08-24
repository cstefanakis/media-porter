package org.sda.mediaporter.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class SubtitleServiceTest {

    @Autowired
    private SubtitleRepository subtitleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private SubtitleService subtitleService;

    private Path fileWithEngSubtitles = Path.of("src/test/resources/movies/notitled (720p_24fps_H264-128kbit_AAC).mp4");
    private Path fileWithUncSubtitles = Path.of("src/test/resources/movies/Thor (2011) - Official Trailer [HD] (816p_24fps_H264-128kbit_AAC).mp4");
    private Movie movie;

    @BeforeEach
    void setup(){
        configurationRepository.deleteAll();
        movieRepository.deleteAll();
        languageRepository.deleteAll();
        codecRepository.deleteAll();
        subtitleRepository.deleteAll();

       languageRepository.save(Language.builder()
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .englishTitle("English")
                .originalTitle("English")
                .build());

       languageRepository.save(Language.builder()
                .iso6391("es")
                .iso6392B("spa")
                .iso6392T("spa")
                .englishTitle("Spanish")
                .originalTitle("Español")
                .build());

       codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.SUBTITLE)
                .name("subrip")
                .build());

       codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.SUBTITLE)
                .name("ass")
                .build());

       this.movie = movieRepository.save(Movie.builder()
                        .title("Superman")
                        .year(2025)
                .build());
    }

    @Test
    void createSubtitleListFromFile() {
        //Act
        List<Subtitle> fileSubtitles = subtitleService.createSubtitleListFromFile(this.fileWithEngSubtitles, this.movie);
        //Assert
        assertEquals(1, fileSubtitles.size());
        assertEquals("English", fileSubtitles.getFirst().getLanguage().getEnglishTitle());
        assertEquals("subrip", fileSubtitles.getFirst().getFormat().getName());
    }

    @Test
    void createSubtitleListFromFile_withUncLanguageSubtitles() {
        //Act
        List<Subtitle> fileSubtitles = subtitleService.createSubtitleListFromFile(this.fileWithUncSubtitles, this.movie);
        //Assert
        assertEquals(1, fileSubtitles.size());
        assertNull(fileSubtitles.getFirst().getLanguage());
        assertEquals("subrip", fileSubtitles.getFirst().getFormat().getName());
    }

    @Test
    void createSubtitleListFromFile_withNoSavedLanguageSubtitles() {
        //Arrest
        languageRepository.deleteAll();
        codecRepository.deleteAll();
        //Act
        List<Subtitle> fileSubtitles = subtitleService.createSubtitleListFromFile(this.fileWithEngSubtitles, this.movie);
        //Assert
        assertEquals(1, fileSubtitles.size());
        assertNull(fileSubtitles.getFirst().getLanguage());
        assertEquals("subrip", fileSubtitles.getFirst().getFormat().getName());
    }
}