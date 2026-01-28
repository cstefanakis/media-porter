package org.sda.mediaporter.services.tvRecordsServices;

import java.nio.file.Path;

public interface TvRecordService {
    String getFileNameOfPath(Path file);

    void copyMovieToSourcePath(Path filePath, String originalTitle, Integer year, Path destinationRootPath);
}
