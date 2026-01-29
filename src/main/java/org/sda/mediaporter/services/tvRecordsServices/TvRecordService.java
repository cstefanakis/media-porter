package org.sda.mediaporter.services.tvRecordsServices;

import java.nio.file.Path;

public interface TvRecordService {
    String getFileNameOfPath(Path file);

    Path getMovieDestinationPath(Path filePath, String originalTitle, Integer year, Path destinationRootPath);

    Path getTvShowsDestinationPath(Path file, String originalTitle, Integer year, Path destinationRootPath);
}
