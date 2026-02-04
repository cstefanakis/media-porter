package org.sda.mediaporter.services.tvRecordsServices.impl;

import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.tvRecordsServices.TvRecordService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class TvRecordServiceImpl implements TvRecordService {

    private final FileService fileService;

    public TvRecordServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public String getFilteredFileName(String fileNameOfPath) {
        String extensionWithDot = fileService.getFileExtensionWithDot(fileNameOfPath);
        String filteredName = fileNameOfPath
                .replace(extensionWithDot, "")
                .replaceAll("\\[.*?\\]|\\(.*?\\)", "")
                .replaceAll("\\d{2,4}-\\d{2}-\\d{2}|\\d{2}-\\d{2}", "")
                .replaceAll("\\s+", " ")
                .trim();

        return filteredName + extensionWithDot;
    }

    @Override
    public Path getMovieDestinationPath(Path filePath, String originalTitle, Integer year, Path destinationRootPath) {
        String fileExtension = fileService.getFileExtensionWithDot(filePath.toString());
        String movieName = String.format("%s (%s)", originalTitle, year);
        return Path.of(destinationRootPath + File.separator +  movieName + fileExtension);
    }

    @Override
    public Path getTvShowsDestinationPath(Path filePath, String originalTitle, Integer year, Path destinationRootPath) {
        String dateFormat = getDateFormat(filePath);
        String fileExtensionWithDot = fileService.getFileExtensionWithDot(filePath.toString());
        String directoryName = String.format("%s (%s)", originalTitle, year);
        String fileName = String.format("%s (%s)", originalTitle, dateFormat);
        return Path.of(destinationRootPath +
                File.separator +
                directoryName +
                File.separator +
                fileName +
                fileExtensionWithDot).normalize();
    }

    private String getDateFormat(Path filePath) {
        LocalDateTime fileLocalDateTime = fileService.getModificationLocalDateTimeOfPath(filePath);
        return String.format("%s.%02d.%02d (%02d_%02d)",
                fileLocalDateTime.getYear(),
                fileLocalDateTime.getMonthValue(),
                fileLocalDateTime.getDayOfMonth(),
                fileLocalDateTime.getHour(),
                fileLocalDateTime.getMinute()
        );
    }
}
