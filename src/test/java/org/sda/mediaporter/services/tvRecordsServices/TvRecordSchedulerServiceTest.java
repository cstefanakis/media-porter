package org.sda.mediaporter.services.tvRecordsServices;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TvRecordSchedulerServiceTest {

//    @Autowired
//    private TvRecordSchedulerService tvRecordSchedulerService;
//
//    @Autowired
//    private FileService fileService;
//
//    @Autowired
//    private SourcePathService sourcePathService;
//
//    @Autowired
//    private ConfigurationRepository configurationRepository;
//
//    private Configuration sourceMovieRecConfiguration;
//    private Configuration sourceTvRecRecConfiguration;
//    private Configuration tvRecordTvShowConfiguration;
//    private Configuration tvRecordMovieConfiguration;
//
//    @BeforeEach
//    void setup(){
//        List<SourcePath> movieRecSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE_RECORDS);
//        for (SourcePath sourcePath : movieRecSourcePaths){
//            if (sourcePath != null){
//                Long configurationId = sourcePath.getConfiguration().getId();
//                this.sourceMovieRecConfiguration = configurationRepository.findById(configurationId).orElse(null);
//            }
//        }
//
//        List<SourcePath> tvShowRecSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.TV_SHOW_RECORDS);
//        for (SourcePath sourcePath : tvShowRecSourcePaths){
//            if (sourcePath != null){
//                Long configurationId = sourcePath.getConfiguration().getId();
//                this.sourceTvRecRecConfiguration = configurationRepository.findById(configurationId).orElse(null);
//            }
//        }
//
//        List<SourcePath> movieTvRecordsRecPaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.TV_RECORDS, LibraryItems.MOVIE);
//        for (SourcePath sourcePath : movieTvRecordsRecPaths){
//            if (sourcePath != null){
//                Long configurationId = sourcePath.getConfiguration().getId();
//                this.tvRecordMovieConfiguration = configurationRepository.findById(configurationId).orElse(null);
//            }
//        }
//
//        List<SourcePath> tvShowTvRecordsSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.TV_RECORDS, LibraryItems.TV_SHOW);
//        for (SourcePath sourcePath : tvShowTvRecordsSourcePaths){
//            if (sourcePath != null){
//                Long configurationId = sourcePath.getConfiguration().getId();
//                this.tvRecordTvShowConfiguration = configurationRepository.findById(configurationId).orElse(null);
//            }
//        }
//    }
//
//    @Test
//    void copyMoviesFromTvShowSources_failedCopyOldFile() {
//        //Arrest
//        Path filePath = Path.of("src\\test\\resources\\externalSources\\tvRecords\\movies\\[K12] Λούφα και Παραλλαγή.mp4");
//        Path destinationPath = Path.of("src\\test\\resources\\tvRecords\\Movies\\Λούφα και Παραλλαγή (1984).mp4");
//        fileService.setLastModifiedTimeToFilePath(filePath, LocalDateTime.now().minusDays(8));
//        setMaxDatesControlFilesFromExternalSource(this.tvRecordMovieConfiguration, 5);
//        //Act
//        tvRecordSchedulerService.copyMoviesFromTvRecordSources();
//        //Assert
//        assertFalse(Files.exists(destinationPath));
//    }
//
//    @Test
//    void deleteOldTvRecords_FilesAreNotOld() {
//        //Arrest
//        Path tvShowFilePath = Path.of("src/test/resources/externalSources/tvRecords/tvShows/[K12] IQ 160 - Season 2 (R).mkv").normalize();
//        Path movieFilePath = Path.of("src/test/resources/externalSources/tvRecords/movies/[K12] Λούφα και Παραλλαγή.mp4").normalize();
//        Path destinationTvShowFilePath = Path.of("src/test/resources/tvRecords/TvShows/[K12] IQ 160 - Season 2 (R).mkv").normalize();
//        Path destinationMovieFilePath = Path.of("src/test/resources/tvRecords/Movies/[K12] Λούφα και Παραλλαγή.mp4").normalize();
//
//        this.sourceTvRecRecConfiguration.setMaxDatesSaveFile(5);
//        configurationRepository.save(this.sourceTvRecRecConfiguration);
//
//        this.sourceMovieRecConfiguration.setMaxDatesSaveFile(5);
//        configurationRepository.save(this.sourceMovieRecConfiguration);
//
//        copyFileToTvShowRecSource(tvShowFilePath);
//        copyFileToMovieRecSource(movieFilePath);
//
//        fileService.setLastModifiedTimeToFilePath(destinationMovieFilePath, LocalDateTime.now());
//        fileService.setLastModifiedTimeToFilePath(destinationTvShowFilePath, LocalDateTime.now());
//        //Act
//        tvRecordSchedulerService.deleteOldTvRecords();
//        //Assert
//        assertTrue(Files.exists(destinationTvShowFilePath));
//        assertTrue(Files.exists(destinationMovieFilePath));
//        //Finally
//        deleteFile(destinationMovieFilePath);
//        deleteFile(destinationTvShowFilePath);
//    }
//
//    @Test
//    void deleteOldTvRecords_FilesAreOld() {
//        //Arrest
//        Path tvShowFilePath = Path.of("src/test/resources/externalSources/tvRecords/tvShows/[K12] IQ 160 - Season 2 (R).mkv").normalize();
//        Path movieFilePath = Path.of("src/test/resources/externalSources/tvRecords/movies/[K12] Λούφα και Παραλλαγή.mp4").normalize();
//        Path destinationTvShowFilePath = Path.of("src/test/resources/tvRecords/TvShows/[K12] IQ 160 - Season 2 (R).mkv").normalize();
//        Path destinationMovieFilePath = Path.of("src/test/resources/tvRecords/Movies/[K12] Λούφα και Παραλλαγή.mp4").normalize();
//
//        this.sourceTvRecRecConfiguration.setMaxDatesSaveFile(5);
//        configurationRepository.save(this.sourceTvRecRecConfiguration);
//
//        this.sourceMovieRecConfiguration.setMaxDatesSaveFile(5);
//        configurationRepository.save(this.sourceMovieRecConfiguration);
//
//        copyFileToTvShowRecSource(tvShowFilePath);
//        copyFileToMovieRecSource(movieFilePath);
//
//        fileService.setLastModifiedTimeToFilePath(destinationMovieFilePath, LocalDateTime.now().minusDays(6));
//        fileService.setLastModifiedTimeToFilePath(destinationTvShowFilePath, LocalDateTime.now().minusDays(6));
//        //Act
//        tvRecordSchedulerService.deleteOldTvRecords();
//        //Assert
//        assertFalse(Files.exists(destinationTvShowFilePath));
//        assertFalse(Files.exists(destinationMovieFilePath));
//        //Finally
//        deleteFile(destinationMovieFilePath);
//        deleteFile(destinationTvShowFilePath);
//    }
//
//    @Transactional
//    private void setMaxDatesControlFilesFromExternalSource(Configuration configuration, Integer date){
//        configuration.setMaxDatesControlFilesFromExternalSource(date);
//        configurationRepository.save(configuration);
//    }
//
//    private void deleteFile(Path filePath){
//        if(Files.exists(filePath)){
//            try {
//                Files.delete(filePath);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private void copyFileToTvShowRecSource(Path filePath) {
//        Path destinationFilePath = Path.of("src\\test\\resources\\tvRecords\\TvShows" + File.separator + filePath.getFileName()).normalize();
//        copyFile(filePath, destinationFilePath);
//    }
//
//    private void copyFileToMovieRecSource(Path filePath) {
//        Path destinationFilePath = Path.of("src\\test\\resources\\tvRecords\\Movies" + File.separator + filePath.getFileName()).normalize();
//        copyFile(filePath, destinationFilePath);
//    }
//
//    private void copyFile(Path sourcePath, Path destinationPath){
//        try {
//            if(!Files.exists(destinationPath)) {
//                Files.copy(sourcePath, destinationPath);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}