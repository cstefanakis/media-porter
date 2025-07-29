package org.sda.mediaporter.Services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.DownloadFileDto;
import org.sda.mediaporter.models.DownloadFile;

import java.util.List;

public interface DownloaderService {
    List<DownloadFile> downloads();
    void deleteDownloadFileById(Long id);
    DownloadFile createdDownloadFile(@Valid DownloadFileDto downloadFileDto);
    void downloadFile();
}
