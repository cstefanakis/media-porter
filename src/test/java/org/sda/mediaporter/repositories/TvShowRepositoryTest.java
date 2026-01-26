package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TvShowRepositoryTest {

    @Autowired
    private TvShowRepository tvShowRepository;

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private TvShowEpisodeRepository tvShowEpisodeRepository;

    private TvShow tvShow1;
    private TvShow tvShow2;
    private TvShow tvShow3;
    private TvShowEpisode tvShowEpisode1;
    private TvShowEpisode tvShowEpisode2;
    private SourcePath sourcePath1;
    private SourcePath sourcePath2;
    private VideoFilePath videoFilePath1;
    private VideoFilePath videoFilePath2;


    @BeforeEach
    void setup(){

        LocalDateTime daysBefore4 = LocalDateTime.now().minusDays(4);
        LocalDateTime daysBefore2 = LocalDateTime.now().minusDays(2);

        this.tvShow1 = tvShowRepository.save(TvShow.builder()
                        .title("tvShowTitle1")
                        .lastModificationDateTime(daysBefore2)
                .build());

        this.tvShow2 = tvShowRepository.save(TvShow.builder()
                .title("tvShowTitle2")
                .lastModificationDateTime(daysBefore4)
                .build());

        this.tvShow3 = tvShowRepository.save(TvShow.builder()
                .title("tvShowTitle3")
                .lastModificationDateTime(daysBefore4)
                .build());

        this.tvShowEpisode1 = tvShowEpisodeRepository.save(TvShowEpisode.builder()
                .tvShow(this.tvShow1)
                        .episodeName("title1")
                        .theMovieDbId(1L)
                        .seasonNumber(1)
                        .episodeNumber(1)
                .build());

        this.tvShowEpisode2 = tvShowEpisodeRepository.save(TvShowEpisode.builder()
                .tvShow(this.tvShow2)
                .episodeName("title2")
                .theMovieDbId(2L)
                .seasonNumber(1)
                .episodeNumber(1)
                .build());

        this.sourcePath1 = sourcePathRepository.save(SourcePath.builder()
                .path("c:/tvShow")
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.TV_SHOW)
                .build());

        this.sourcePath2 = sourcePathRepository.save(SourcePath.builder()
                .path("c:/download")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .libraryItem(LibraryItems.TV_SHOW)
                .build());

        this.videoFilePath1 = videoFilePathRepository.save(VideoFilePath.builder()
                .modificationDateTime(daysBefore2)
                .sourcePath(this.sourcePath1)
                .filePath("/test1.mp4")
                        .tvShowEpisode(this.tvShowEpisode1)
                .build());

        this.videoFilePath2 = videoFilePathRepository.save(VideoFilePath.builder()
                .modificationDateTime(daysBefore4)
                .sourcePath(this.sourcePath2)
                .filePath("/test2.mp4")
                        .tvShowEpisode(tvShowEpisode2)
                .build());
    }

    @Test
    void findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath_3DaysOld() {
        //Arrest
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(3);
        Long videoFilePathId = videoFilePath1.getId();
        //Act
        List<Long> result = tvShowRepository.findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(localDateTime, this.sourcePath1);
        //Assert
        assertTrue(result.isEmpty());
        assertFalse(result.contains(videoFilePathId));
    }

    @Test
    void findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath_2DaysOld() {
        //Arrest
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(2);
        Long videoFilePathId = videoFilePath1.getId();
        //Act
        List<Long> result = tvShowRepository.findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(localDateTime, this.sourcePath1);
        //Assert
        assertFalse(result.isEmpty());
        assertTrue(result.contains(videoFilePathId));
    }

    @Test
    void findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath_1aysOld() {
        //Arrest
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
        Long videoFilePathId = videoFilePath1.getId();
        //Act
        List<Long> result = tvShowRepository.findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(localDateTime, this.sourcePath1);
        //Assert
        assertFalse(result.isEmpty());
        assertTrue(result.contains(videoFilePathId));
    }

    @Test
    void findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath_4aysOld() {
        //Arrest
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(4);
        Long videoFilePathId = this.videoFilePath2.getId();
        //Act
        List<Long> result = tvShowRepository.findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(localDateTime, this.sourcePath2);
        //Assert
        assertFalse(result.isEmpty());
        assertTrue(result.contains(videoFilePathId));
    }

    @Test
    void findTvShowIdByVideoFilePathId() {
        //Arrest
        Long videoFilePathId = this.videoFilePath1.getId();
        Long tvShowId = this.tvShow1.getId();
        //Act
        Long result = tvShowRepository.findTvShowIdByVideoFilePathId(videoFilePathId);
        //Assert
        assertEquals(tvShowId, result);
    }

    @Test
    void deleteTvShowWithoutTvShowEpisodes() {
        //Arrest
        Long tvShowId = this.tvShow3.getId();
        //Act
        tvShowRepository.deleteTvShowWithoutTvShowEpisodes(tvShowId);
        Optional <TvShow> result = tvShowRepository.findById(tvShowId);
        //Assert
        assertFalse(result.isPresent());
    }

    @AfterEach
    void end(){
        videoFilePathRepository.delete(this.videoFilePath1);
        videoFilePathRepository.delete(this.videoFilePath2);

        tvShowEpisodeRepository.delete(this.tvShowEpisode1);
        tvShowEpisodeRepository.delete(this.tvShowEpisode2);

        tvShowRepository.delete(this.tvShow1);
        tvShowRepository.delete(this.tvShow2);
        tvShowRepository.delete(this.tvShow3);

        sourcePathRepository.delete(this.sourcePath1);
        sourcePathRepository.delete(this.sourcePath2);
    }

}