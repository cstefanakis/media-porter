package org.sda.mediaporter.services.tvRecordsServices.impl;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieSearchDTO;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowSearchDTO;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.ConfigurationService;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.sda.mediaporter.services.tvRecordsServices.TvRecordSchedulerService;
import org.sda.mediaporter.services.tvRecordsServices.TvRecordService;
import org.sda.mediaporter.services.tvShowServices.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TvRecordSchedulerServiceImpl implements TvRecordSchedulerService {

    private final TvRecordService tvRecordService;
    private final SourcePathService sourcePathService;
    private final FileService fileService;
    private final MovieService movieService;
    private final TvShowService tvShowService;
    private final ConfigurationService configurationService;

    @Autowired
    public TvRecordSchedulerServiceImpl(TvRecordService tvRecordService, SourcePathService sourcePathService, FileService fileService, MovieService movieService, TvShowService tvShowService, ConfigurationService configurationService) {
        this.tvRecordService = tvRecordService;
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
        this.movieService = movieService;
        this.tvShowService = tvShowService;
        this.configurationService = configurationService;
    }

    @Override
    @Scheduled(fixedRate = 20000)
    public void copyMoviesFromTvRecordSources() {
        List<SourcePath> movieTvRecordSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE_RECORDS);
        if(movieTvRecordSourcePaths != null){
            SourcePath movieTvRecordSourcePath = movieTvRecordSourcePaths.getFirst();
            List<SourcePath> movieTvExternalRecordSources = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.TV_RECORDS, LibraryItems.MOVIE);
            for (SourcePath sourcePath : movieTvExternalRecordSources){
                Path rootDirectory = Path.of(sourcePath.getPath()).normalize();
                List<Path> directoryFiles = fileService.getVideoFiles(rootDirectory);
                for(Path file : directoryFiles){
                    String fileNameOfPath = file.getFileName().toString();
                    String movieTitle = tvRecordService.getFilteredFileName(fileNameOfPath);
                    TheMovieDbMovieSearchDTO theMovieDbMovieSearchDTO = movieService.getMovieAPISearchDTO(movieTitle);
                    if(theMovieDbMovieSearchDTO != null) {
                        Long movieTheMovieDbId = theMovieDbMovieSearchDTO.getTheMovieDbId();
                        String originalTitle = theMovieDbMovieSearchDTO.getOriginalTitle();
                        Integer year = theMovieDbMovieSearchDTO.getYear();
                        Path destinationRootPath = Path.of(movieTvRecordSourcePath.getPath()).normalize();
                        Path destinationFilePath = tvRecordService.getMovieDestinationPath(file, originalTitle, year, destinationRootPath);
                        //Check if file path exist
                        if (!fileService.isFilePathExist(destinationFilePath)) {
                            boolean isMovieExist = movieService.isMovieByTheMovieDbIdExist(movieTheMovieDbId);
                            //check if movie exist
                            if (!isMovieExist) {
                                Integer modificationDates = sourcePath.getConfiguration().getMaxDatesControlFilesFromExternalSource();
                                LocalDateTime fileModificationDateTime = fileService.getModificationLocalDateTimeOfPath(file);
                                boolean isValidModificationDateTime = configurationService.isFileModificationDateValid(fileModificationDateTime, modificationDates);
                                //check if movie can download by properties
                                if (isValidModificationDateTime) {
                                    fileService.copyFile(file, destinationFilePath);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Scheduled(fixedRate = 20000)
    public void copyTvShowsFromTvRecordSources() {
        List<SourcePath> tvShowTvRecordSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.TV_SHOW_RECORDS);
        if(tvShowTvRecordSourcePaths != null){
            SourcePath movieTvRecordSourcePath = tvShowTvRecordSourcePaths.getFirst();
            List<SourcePath> tvShowTvExternalRecordSources = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.TV_RECORDS, LibraryItems.TV_SHOW);
            for (SourcePath sourcePath : tvShowTvExternalRecordSources){
                Path rootDirectory = Path.of(sourcePath.getPath()).normalize();
                List<Path> directoryFiles = fileService.getVideoFiles(rootDirectory);
                for(Path file : directoryFiles){
                    String fileNameOfPath = file.getFileName().toString();
                    String tvShowTitle = tvRecordService.getFilteredFileName(fileNameOfPath);
                    TheMovieDbTvShowSearchDTO theMovieDbTvShowSearchDTO = tvShowService.getTvShowAPISearchDTO(tvShowTitle);
                    if(theMovieDbTvShowSearchDTO != null) {
                        String originalTitle = theMovieDbTvShowSearchDTO.getOriginalTitle();
                        Integer year = theMovieDbTvShowSearchDTO.getYear();
                        Path destinationRootPath = Path.of(movieTvRecordSourcePath.getPath()).normalize();
                        Path destinationFilePath = tvRecordService.getTvShowsDestinationPath(file, originalTitle, year, destinationRootPath);
                        //Check if file path exist
                        if (!fileService.isFilePathExist(destinationFilePath)) {
                            Integer modificationDates = sourcePath.getConfiguration().getMaxDatesControlFilesFromExternalSource();
                            LocalDateTime fileModificationDateTime = fileService.getModificationLocalDateTimeOfPath(file);
                            boolean isValidModificationDateTime = configurationService.isFileModificationDateValid(fileModificationDateTime, modificationDates);
                            //check if movie can download by properties
                            if (isValidModificationDateTime) {
                                fileService.copyFile(file, destinationFilePath);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Scheduled(fixedRate = 20000)
    public void deleteOldTvRecords() {
        List<SourcePath> tvShowSourceTvRecords = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.TV_SHOW_RECORDS);
        List<SourcePath> moviesSourceTvRecords = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE_RECORDS);
        List<SourcePath> sourcePaths = Stream.concat(tvShowSourceTvRecords.stream(), moviesSourceTvRecords.stream()).toList();
        for (SourcePath sourcePath : sourcePaths){
            Path directoryPath = Path.of(sourcePath.getPath()).normalize();
            List<Path> filePaths = fileService.getVideoFiles(directoryPath);
            for (Path filePath : filePaths){
                Integer maxDatesSaveFile = sourcePath.getConfiguration().getMaxDatesSaveFile();
                LocalDateTime fileModificationDate = fileService.getModificationLocalDateTimeOfPath(filePath);
                boolean isFileOld = configurationService.isFileOld(fileModificationDate, maxDatesSaveFile);
                if(isFileOld){
                    fileService.deleteFile(filePath);
                    fileService.deleteSubDirectories(filePath);
                }
            }
        }
    }
}
