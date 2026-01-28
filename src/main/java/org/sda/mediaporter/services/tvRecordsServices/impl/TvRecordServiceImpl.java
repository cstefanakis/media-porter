package org.sda.mediaporter.services.tvRecordsServices.impl;

import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.tvRecordsServices.TvRecordService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public class TvRecordServiceImpl implements TvRecordService {

    private final FileService fileService;

    public TvRecordServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public String getFileNameOfPath(Path file) {
        String fileName = file.getFileName().toString();
        return getFilteredFileName(fileName);
    }

    @Override
    public void copyMovieToSourcePath(Path filePath, String originalTitle, Integer year, Path destinationRootPath) {
        Path destinationPath = createMovieDestinationPath(filePath, originalTitle, year, destinationRootPath);
        fileService.copyFile(filePath, destinationPath);
    }

    private Path createMovieDestinationPath(Path filePath, String originalTitle, Integer year, Path destinationRootPath) {
        String fileExtension = fileService.getFileExtensionWithDot(filePath.toString());
        String movieName = String.format("%s (%s)", originalTitle, year);
        return Path.of(destinationRootPath + File.separator +  movieName + fileExtension);
    }

    private String getFilteredFileName(String fileName) {
        String extensionWithDot = fileService.getFileExtensionWithDot(fileName);
        String filteredName = fileName
                .replace(extensionWithDot, "")
                .replaceAll("\\[.*?\\]|\\(.*?\\)", "")
                .replaceAll("\\d{2,4}-\\d{2}-\\d{2}|\\d{2}-\\d{2}", "")
                .replaceAll("\\s+", " ")
                .trim();

        return filteredName + extensionWithDot;
    }
}
