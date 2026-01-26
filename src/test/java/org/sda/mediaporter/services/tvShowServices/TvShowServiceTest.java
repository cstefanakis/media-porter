package org.sda.mediaporter.services.tvShowServices;

import org.junit.jupiter.api.Test;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowSearchDTO;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.*;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.SubtitleRepository;
import org.sda.mediaporter.repositories.metadata.VideoRepository;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.sda.mediaporter.services.videoServices.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TvShowServiceTest {

    @Autowired
    private TvShowService tvShowService;

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

    @Autowired
    private ConfigurationRepository configurationRepository;

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
    private Configuration movieConfiguration;
    private Configuration tvShowConfiguration;

//    @BeforeEach
//    void setup(){
//        this.tvShow = tvShowRepository.save(TvShow.builder()
//                .title("tvShowTitle")
//                .theMoveDBTvShowId(17L)
//                .year(2012)
//                .build());
//
//        this.tvShowEpisode = tvShowEpisodeRepository.save(TvShowEpisode.builder()
//                .episodeNumber(1)
//                .seasonNumber(1)
//                .theMovieDbId(12L)
//                .tvShow(this.tvShow)
//                .build());
//
//        this.movieConfiguration = configurationRepository.save(Configuration.builder()
//                        .maxDatesSaveFile(null)
//                .build());
//
//        this.tvShowConfiguration = configurationRepository.save(Configuration.builder()
//                .maxDatesSaveFile(null)
//                .build());
//
//        String movieSourcePath = Path.of("src/test/resources/movies").normalize().toString();
//        this.moviesSourcePath = sourcePathRepository.save(SourcePath.builder()
//                .path(movieSourcePath)
//                .pathType(SourcePath.PathType.SOURCE)
//                .libraryItem(LibraryItems.MOVIE)
//                .build());
//
//        String tvShowSourcePath = Path.of("src/test/resources/tvShows").normalize().toString();
//        this.tvShowSourcePath = sourcePathRepository.save(SourcePath.builder()
//                .path(tvShowSourcePath)
//                .pathType(SourcePath.PathType.SOURCE)
//                .libraryItem(LibraryItems.TV_SHOW)
//                .build());
//
//        createSourcePath(this.tvShowSourcePath, this.tvShowConfiguration);
//        createSourcePath(this.moviesSourcePath, this.movieConfiguration);
//
//        this.movieConfiguration.setSourcePath(moviesSourcePath);
//        configurationRepository.save(this.movieConfiguration);
//
//        this.tvShowConfiguration.setSourcePath(this.tvShowSourcePath);
//        configurationRepository.save(this.tvShowConfiguration);
//
//        this.movie = movieRepository.save(Movie.builder()
//                .title("movie")
//                .build());
//
//        this.video = videoRepository.save(Video.builder()
//                .build());
//
//        String filePath1 = Path.of("/test.mp4").normalize().toString();
//        this.videoFilePathOld2Days = videoFilePathRepository.save(VideoFilePath.builder()
//                .modificationDateTime(LocalDateTime.now().minusDays(2))
//                .sourcePath(this.moviesSourcePath)
//                .filePath(filePath1)
//                .movie(this.movie)
//                .video(this.video)
//                .build());
//
//        String filePath2 = Path.of("/test2.mp4").normalize().toString();
//        this.videoFilePathOld4Days = videoFilePathRepository.save(VideoFilePath.builder()
//                .modificationDateTime(LocalDateTime.now().minusDays(4))
//                .sourcePath(this.moviesSourcePath)
//                .filePath(filePath2)
//                .movie(this.movie)
//                .build());
//
//        String filePath3 = Path.of("/tvShowFilePath.mp4").normalize().toString();
//        this.tvShowVideoFilePath = videoFilePathRepository.save(VideoFilePath.builder()
//                .modificationDateTime(LocalDateTime.now().minusDays(4))
//                .sourcePath(this.tvShowSourcePath)
//                .filePath(filePath3)
//                .tvShowEpisode(this.tvShowEpisode)
//                .build());
//
//        this.video.setVideoFilePath(this.videoFilePathOld2Days);
//        videoRepository.save(this.video);
//
//        this.audio1 = audioRepository.save(Audio.builder()
//                .videoFilePath(videoFilePathOld2Days)
//                .build());
//
//        this.audio2 = audioRepository.save(Audio.builder()
//                .videoFilePath(videoFilePathOld2Days)
//                .build());
//
//        this.subtitle1 = subtitleRepository.save(Subtitle.builder()
//                .videoFilePath(videoFilePathOld2Days)
//                .build());
//
//        this.subtitle2 = subtitleRepository.save(Subtitle.builder()
//                .videoFilePath(videoFilePathOld2Days)
//                .build());
//    }

    @Test
    void deleteVideoFilePathsFromTvShowsWithUnveiledPath() {
        //Act
        tvShowService.deleteVideoFilePathsFromTvShowsWithUnveiledPath();
        //Arrest
        Optional <VideoFilePath> videoFilePathOptional = videoFilePathRepository.findById(this.videoFilePathOld4Days.getId());
        System.out.println();
        //Assert
        assertFalse(videoFilePathOptional.isPresent());
    }

    @Test
    void getTvShowAPISearchDTO() {
        //Arrest
        String title = "Prison Break";
        //Act
        TheMovieDbTvShowSearchDTO theMovieDbTvShowSearchDTO = tvShowService.getTvShowAPISearchDTO(title);
        //Assert
        assertEquals(title, theMovieDbTvShowSearchDTO.getTitle());
        assertEquals(2288, theMovieDbTvShowSearchDTO.getTheMovieDbId());
    }

//    @AfterEach
//    void end(){
//        Long videoFilePathOld2DaysId = this.videoFilePathOld2Days.getId();
//        Long videoFilePathOld4DaysId = this.videoFilePathOld4Days.getId();
//        videoFilePathService.deleteVideoFilePathById(videoFilePathOld2DaysId);
//        videoFilePathService.deleteVideoFilePathById(videoFilePathOld4DaysId);
//        //delete Movie
//        Optional <Movie> movieOptional = movieRepository.findById(this.movie.getId());
//        movieOptional.ifPresent(m -> movieRepository.deleteById(m.getId()));
//        //delete tvShowEpisode
//        Optional <TvShowEpisode> tvShowEpisodeOptional = tvShowEpisodeRepository.findById(this.tvShowEpisode.getId());
//        tvShowEpisodeOptional.ifPresent(tse -> tvShowEpisodeRepository.deleteById(tse.getId()));
//        //delete tvShow
//        Optional <TvShow> tvShowOptional = tvShowRepository.findById(this.tvShow.getId());
//        tvShowOptional.ifPresent(ts -> tvShowRepository.deleteById(this.tvShow.getId()));
//        //delete source path
//        Optional<SourcePath> tvShowSourcePathOptional = sourcePathRepository.findById(this.tvShowSourcePath.getId());
//        tvShowSourcePathOptional.ifPresent(sp -> sourcePathRepository.deleteById(sp.getId()));
//        Optional<SourcePath> movieSourcePathOptional = sourcePathRepository.findById(this.moviesSourcePath.getId());
//        movieSourcePathOptional.ifPresent(sp -> sourcePathRepository.deleteById(sp.getId()));
//    }
//
//    @Transactional
//    private void createSourcePath(SourcePath sourcePath, Configuration configuration){
//        configuration.setSourcePath(sourcePath);
//        sourcePath.setConfiguration(configuration);
//        sourcePathRepository.save(sourcePath);
//    }


}