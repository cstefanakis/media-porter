package org.sda.mediaporter.services.fileServices;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.*;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.sda.mediaporter.services.videoServices.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoFilePathServiceTest {
    @Autowired
    private VideoFilePathService videoFilePathService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private SubtitleRepository subtitleRepository;

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TvShowEpisodeRepository tvShowEpisodeRepository;

    @Autowired
    private TvShowRepository tvShowRepository;

    private SourcePath moviesSourcePath;
    private SourcePath tvShowSourcePath;
    private Movie movie;
    private TvShow tvShow;
    private VideoFilePath videoFilePathOld2Days;
    private VideoFilePath videoFilePathOld4Days;
    private VideoFilePath tvShowVideoFilePath;
    private TvShowEpisode tvShowEpisode;
    private Video video;
    private Audio audio1;
    private Audio audio2;
    private Subtitle subtitle1;
    private Subtitle subtitle2;

    @BeforeEach
    void setup(){
        this.tvShow = tvShowRepository.save(TvShow.builder()
                        .title("tvShowTitle")
                        .theMoveDBTvShowId(15L)
                        .year(2012)
                .build());

        this.tvShowEpisode = tvShowEpisodeRepository.save(TvShowEpisode.builder()
                        .episodeNumber(1)
                        .seasonNumber(1)
                        .theMovieDbId(12L)
                        .tvShow(this.tvShow)
                .build());

        String movieSourcePath = Path.of("src/test/resources/movies").normalize().toString();
        this.moviesSourcePath = sourcePathRepository.save(SourcePath.builder()
                .path(movieSourcePath)
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.MOVIE)
                .build());

        String tvShowSourcePath = Path.of("src/test/resources/tvShows").normalize().toString();
        this.tvShowSourcePath = sourcePathRepository.save(SourcePath.builder()
                .path(tvShowSourcePath)
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.TV_SHOW)
                .build());

        this.movie = movieRepository.save(Movie.builder()
                .title("movie")
                .build());

        this.video = videoRepository.save(Video.builder()
                .build());

        String filePath1 = Path.of("/test.mp4").normalize().toString();
        this.videoFilePathOld2Days = videoFilePathRepository.save(VideoFilePath.builder()
                .modificationDateTime(LocalDateTime.now().minusDays(2))
                .sourcePath(this.moviesSourcePath)
                .filePath(filePath1)
                .movie(this.movie)
                        .video(this.video)
                .build());

        String filePath2 = Path.of("/test2.mp4").normalize().toString();
        this.videoFilePathOld4Days = videoFilePathRepository.save(VideoFilePath.builder()
                .modificationDateTime(LocalDateTime.now().minusDays(4))
                .sourcePath(this.moviesSourcePath)
                .filePath(filePath2)
                .movie(this.movie)
                .build());

        String filePath3 = Path.of("/tvShowFilePath.mp4").normalize().toString();
        this.tvShowVideoFilePath = videoFilePathRepository.save(VideoFilePath.builder()
                .modificationDateTime(LocalDateTime.now().minusDays(4))
                .sourcePath(this.tvShowSourcePath)
                .filePath(filePath3)
                        .tvShowEpisode(this.tvShowEpisode)
                .build());

        this.video.setVideoFilePath(this.videoFilePathOld2Days);
        videoRepository.save(this.video);

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
    void createVideoFilePath() {
        //Arrest
        Path filePath = Path.of("C:\\Users\\chris\\Downloads\\Movies\\Titanic (1997) ¦ Official Trailer (720p_24fps_H264-128kbit_AAC).mp4");
        //Act
        VideoFilePath result = videoFilePathService.createVideoFilePath(filePath);

        //Assert
        assertNotNull(result);
        assertTrue(result.getId() >= 0);

        if(result.getId() != null){
            videoFilePathService.deleteVideoFilePathAndFileByVideoFilePathId(result.getId());
        }

    }

    @Test
    void deleteAllDataFromVideoFilePath() {
        //Act
        videoFilePathService.deleteAllDataFromVideoFilePath(this.videoFilePathOld2Days);
        Optional <Video> video = videoRepository.findById(this.video.getId());
        Optional<Audio> audio1 =  audioRepository.findById(this.audio1.getId());
        Optional <Audio> audio2 =  audioRepository.findById(this.audio2.getId());
        Optional <Subtitle> subtitle1 = subtitleRepository.findById(this.subtitle1.getId());
        Optional <Subtitle> subtitle2 = subtitleRepository.findById(this.subtitle2.getId());
        //Assert
        assertFalse(video.isPresent());
        assertFalse(audio1.isPresent());
        assertFalse(audio2.isPresent());
        assertFalse(subtitle1.isPresent());
        assertFalse(subtitle2.isPresent());

    }

    @Test
    void deleteVideoFilePathAndFileByVideoFilePathId(){
        //Arrest
        Long videoFilePathId = this.videoFilePathOld2Days.getId();
        //Act
        videoFilePathService.deleteVideoFilePathAndFileByVideoFilePathId(videoFilePathId);
        Optional<VideoFilePath> result = videoFilePathRepository.findById(videoFilePathId);
        //Assert
        assertFalse(result.isPresent());
    }
    @Test
    void getFullPathFromVideoFilePathId() {
        //Arrest
        Long videoFilePathId = videoFilePathOld2Days.getId();
        Path fullPath = Path.of(this.moviesSourcePath.getPath() + this.videoFilePathOld2Days.getFilePath()).normalize();
        //Act
        Path result = videoFilePathService.getFullPathFromVideoFilePathId(videoFilePathId);
        //Assert
        assertEquals(fullPath, result);
        assertFalse(Files.exists(result));
    }

    @Test
    void getTvShowsVideoFilePathIdsByLibraryItems() {
        //Arrest
        LibraryItems tvShow = LibraryItems.TV_SHOW;
        Long videoFilePathId = this.tvShowVideoFilePath.getId();
        //Act
        List<Long> result = videoFilePathService.getTvShowsVideoFilePathIdsByLibraryItems(tvShow);
        //Assert
        assertTrue(result.contains(videoFilePathId));
    }

    @AfterEach
    void end(){
        //Video
        Long videoId = this.video.getId();
        Optional<Video> video = videoRepository.findById(videoId);

        //Audio
        Long audio1Id = this.audio1.getId();
        Long audio2Id = this.audio2.getId();
        Optional<Audio> audio1 =  audioRepository.findById(audio1Id);
        Optional <Audio> audio2 =  audioRepository.findById(audio2Id);

        //Subtitles
        Long subtitle1Id = this.subtitle1.getId();
        Long subtitle2Id = this.subtitle2.getId();
        Optional <Subtitle> subtitle1 = subtitleRepository.findById(subtitle1Id);
        Optional <Subtitle> subtitle2 = subtitleRepository.findById(subtitle2Id);

        //VideoFilePath
        Long videoFilePathOld2DaysId = this.videoFilePathOld2Days.getId();
        Long videoFilePathOld4DaysId = this.videoFilePathOld4Days.getId();
        Optional <VideoFilePath> videoFilePathOld2Days = videoFilePathRepository.findById(videoFilePathOld2DaysId);
        Optional <VideoFilePath> videoFilePathOld4Days = videoFilePathRepository.findById(videoFilePathOld4DaysId);

        //delete video
        videoFilePathOld2Days.ifPresent(videoFilePath -> {
            videoFilePath.setVideo(null);
            videoFilePathRepository.save(videoFilePath);
            video.ifPresent(v -> videoRepository.deleteById(videoId));
        });

        //delete audios
        audio1.ifPresent(audio -> audioRepository.deleteById(audio1Id));
        audio2.ifPresent(audio -> audioRepository.deleteById(audio2Id));
        //delete subtitles
        subtitle1.ifPresent(subtitle -> subtitleRepository.deleteById(subtitle1Id));
        subtitle2.ifPresent(subtitle -> subtitleRepository.deleteById(subtitle2Id));
        //delete videoFilePaths
        videoFilePathOld2Days.ifPresent(videoFilePath -> videoFilePathRepository.deleteById(videoFilePathOld2DaysId));
        videoFilePathOld4Days.ifPresent(videoFilePath -> videoFilePathRepository.deleteById(videoFilePathOld4DaysId));
        //delete Movie
        Optional <Movie> movieOptional = movieRepository.findById(this.movie.getId());
        movieOptional.ifPresent(m -> movieRepository.deleteById(m.getId()));
        //delete tvShowEpisode
        Optional <TvShowEpisode> tvShowEpisodeOptional = tvShowEpisodeRepository.findById(this.tvShowEpisode.getId());
        tvShowEpisodeOptional.ifPresent(tse -> tvShowEpisodeRepository.deleteById(tse.getId()));
        //delete tvShow
        Optional <TvShow> tvShowOptional = tvShowRepository.findById(this.tvShow.getId());
        tvShowOptional.ifPresent(ts -> tvShowRepository.deleteById(this.tvShow.getId()));
    }
}