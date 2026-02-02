package org.sda.mediaporter.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoFilePathRepositoryTest {

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TvShowEpisodeRepository tvShowEpisodeRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private SubtitleRepository subtitleRepository;

    private SourcePath moviesSourcePath;
    private Movie movie;
    private VideoFilePath videoFilePathOld2Days;
    private VideoFilePath videoFilePathOld4Days;
    private TvShowEpisode tvShowEpisode;
    private Audio audio1;
    private Audio audio2;
    private Subtitle subtitle1;
    private Subtitle subtitle2;

    @BeforeEach
    void setup(){

        String movieSourcePath = Path.of("/Movie").normalize().toString();
        String movie1 = Path.of("/movie1.mp4").normalize().toString();
        String movie2 = Path.of("/movie2.mp4").normalize().toString();

        this.moviesSourcePath = sourcePathRepository.save(SourcePath.builder()
                        .path(movieSourcePath)
                        .pathType(SourcePath.PathType.SOURCE)
                        .libraryItem(LibraryItems.MOVIE)
                .build());

        this.movie = movieRepository.save(Movie.builder()
                .title("movie")
                .build());

        this.videoFilePathOld2Days = videoFilePathRepository.save(VideoFilePath.builder()
                        .modificationDateTime(LocalDateTime.now().minusDays(2))
                        .sourcePath(this.moviesSourcePath)
                        .filePath(movie1)
                        .movie(this.movie)
                .build());
        this.videoFilePathOld4Days = videoFilePathRepository.save(VideoFilePath.builder()
                        .modificationDateTime(LocalDateTime.now().minusDays(4))
                        .sourcePath(this.moviesSourcePath)
                        .filePath(movie2)
                        .movie(this.movie)
                .build());

        this.audio1 = audioRepository.save(Audio.builder()
                        .videoFilePath(videoFilePathOld2Days)
                .build());

        this.audio2 = audioRepository.save(Audio.builder()
                        .videoFilePath(videoFilePathOld2Days)
                .build());

        this.subtitle1 = subtitleRepository.save(Subtitle.builder()
                        .videoFilePath(videoFilePathOld2Days)
                .build());

        this.subtitle2 = subtitleRepository.save(Subtitle.builder()
                .videoFilePath(videoFilePathOld2Days)
                .build());

    }

    @Test
    void findMoviesVideoFilePathsOlderThan() {
        //Arrest
        int days = 4;
        LocalDateTime fourDaysBefore = LocalDateTime.now().minusDays(days);

        //Act
        List<Long> result = videoFilePathRepository.findMoviesVideoFilePathsOlderThan(fourDaysBefore, this.moviesSourcePath);
        //Assert
        assertTrue(result.contains(this.videoFilePathOld4Days.getId()));
        assertFalse(result.contains(this.videoFilePathOld2Days.getId()));
    }

    @Test
    void findMovieIdByVideoFilePathId() {
    }

    @Test
    void findVideoFilePathsBySourcePathId() {
        //Arrest
        Long movieSourcePathId = this.moviesSourcePath.getId();
        //Act
        List<VideoFilePath> result = videoFilePathRepository.findMovieVideoFilePathsBySourcePathId(movieSourcePathId);
        //Assert
        assertEquals(2, result.size());
    }

    @Test
    void deleteAudiosFromVideoFilePath() {
        //Act
        videoFilePathRepository.deleteAudiosFromVideoFilePath(this.videoFilePathOld2Days);
        videoFilePathRepository.deleteAudiosFromVideoFilePath(this.videoFilePathOld2Days);
        Optional <Audio> result1 =  audioRepository.findById(this.audio1.getId());
        Optional <Audio> result2 =  audioRepository.findById(this.audio2.getId());
        //Assert
        assertFalse(result1.isPresent());
        assertFalse(result2.isPresent());
    }

    @Test
    void deleteAudiosFromVideoFilePath_audiosIsEmpty() {
        //Arrest
        videoFilePathRepository.deleteAudiosFromVideoFilePath(this.videoFilePathOld2Days);
        videoFilePathRepository.deleteSubtitlesFromVideoFilePath(this.videoFilePathOld2Days);
        //Act
        videoFilePathRepository.deleteAudiosFromVideoFilePath(this.videoFilePathOld2Days);
        videoFilePathRepository.deleteSubtitlesFromVideoFilePath(this.videoFilePathOld2Days);
        Optional <Audio> result1 =  audioRepository.findById(this.audio1.getId());
        Optional <Audio> result2 =  audioRepository.findById(this.audio2.getId());
        //Assert
        assertFalse(result1.isPresent());
        assertFalse(result2.isPresent());
    }

    @AfterEach
    void end(){
        //Audios
        Long audio1Id = this.audio1.getId();
        Long audio2Id = this.audio2.getId();

        Optional <Audio> audio1 =  audioRepository.findById(audio1Id);
        Optional <Audio> audio2 =  audioRepository.findById(audio2Id);

        audio1.ifPresent(audio -> audioRepository.deleteById(audio1Id));
        audio2.ifPresent(audio -> audioRepository.deleteById(audio2Id));

        //Subtitles
        Long subtitles1Id = this.subtitle1.getId();
        Long subtitles2Id = this.subtitle2.getId();

        Optional <Subtitle> subtitle1 = subtitleRepository.findById(subtitles1Id);
        Optional <Subtitle> subtitle2 = subtitleRepository.findById(subtitles2Id);

        subtitle1.ifPresent(subtitle -> subtitleRepository.deleteById(subtitles1Id));
        subtitle2.ifPresent(subtitle -> subtitleRepository.deleteById(subtitles2Id));

        //VideoFilePaths
        Long videoFilePathOld2DaysId = this.videoFilePathOld2Days.getId();
        Long videoFilePathOld4DaysId = this.videoFilePathOld4Days.getId();

        Optional <VideoFilePath> videoFilePathOld2Days = videoFilePathRepository.findById(videoFilePathOld2DaysId);
        Optional <VideoFilePath> videoFilePathOld4Days = videoFilePathRepository.findById(videoFilePathOld4DaysId);

        videoFilePathOld2Days.ifPresent(videoFilePath -> videoFilePathRepository.deleteById(videoFilePathOld2DaysId));
        videoFilePathOld4Days.ifPresent(videoFilePath -> videoFilePathRepository.deleteById(videoFilePathOld4DaysId));
    }
}