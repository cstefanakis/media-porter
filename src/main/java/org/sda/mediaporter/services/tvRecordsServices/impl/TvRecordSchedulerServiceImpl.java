package org.sda.mediaporter.services.tvRecordsServices.impl;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbMovieSearchDTO;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.ConfigurationService;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.fileServices.SourcePathService;
import org.sda.mediaporter.services.movieServices.MovieService;
import org.sda.mediaporter.services.tvRecordsServices.TvRecordSchedulerService;
import org.sda.mediaporter.services.tvRecordsServices.TvRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TvRecordSchedulerServiceImpl implements TvRecordSchedulerService {

    private final TvRecordService tvRecordService;
    private final SourcePathService sourcePathService;
    private final FileService fileService;
    private final MovieService movieService;
    private final ConfigurationService configurationService;

    @Autowired
    public TvRecordSchedulerServiceImpl(TvRecordService tvRecordService, SourcePathService sourcePathService, FileService fileService, MovieService movieService, ConfigurationService configurationService) {
        this.tvRecordService = tvRecordService;
        this.sourcePathService = sourcePathService;
        this.fileService = fileService;
        this.movieService = movieService;
        this.configurationService = configurationService;
    }

    @Override
    public void copyMoviesFromTvShowSources() {
        List<SourcePath> movieTvRecordSourcePaths = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.SOURCE, LibraryItems.MOVIE_RECORDS);
        if(movieTvRecordSourcePaths != null){
            SourcePath movieTvRecordSourcePath = movieTvRecordSourcePaths.getFirst();
            List<SourcePath> movieTvExternalRecordSources = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(SourcePath.PathType.TV_RECORDS, LibraryItems.MOVIE);
            for (SourcePath sourcePath : movieTvExternalRecordSources){
                Path rootDirectory = Path.of(sourcePath.getPath()).normalize();
                List<Path> directoryFiles = fileService.getVideoFiles(rootDirectory);
                for(Path file : directoryFiles){
                    String movieTitle = tvRecordService.getFileNameOfPath(file);
                    TheMovieDbMovieSearchDTO theMovieDbMovieSearchDTO = movieService.getMovieAPISearchDTO(movieTitle);
                    if(theMovieDbMovieSearchDTO != null) {
                        Long movieTheMovieDbId = theMovieDbMovieSearchDTO.getTheMovieDbId();
                        boolean isMovieExist = movieService.isMovieByTheMovieDbIdExist(movieTheMovieDbId);
                        if(!isMovieExist){
                            Integer modificationDates = sourcePath.getConfiguration().getMaxDatesControlFilesFromExternalSource();
                            LocalDateTime fileModificationDateTime = fileService.getModificationLocalDateTimeOfPath(file);
                            boolean isValidModificationDateTime = configurationService.isFileModificationDateValid(fileModificationDateTime, modificationDates);
                            if(isValidModificationDateTime) {
                                Path destinationRootPath = Path.of(movieTvRecordSourcePath.getPath()).normalize();
                                String originalTitle = theMovieDbMovieSearchDTO.getOriginalTitle();
                                Integer year = theMovieDbMovieSearchDTO.getYear();
                                tvRecordService.copyMovieToSourcePath(file, originalTitle, year, destinationRootPath);
                            }
                        }
                    }
                }
            }
        }
    }
}
