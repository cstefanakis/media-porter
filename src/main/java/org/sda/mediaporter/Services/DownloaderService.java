package org.sda.mediaporter.Services;

import org.sda.mediaporter.dtos.DownloadFileDto;
import org.sda.mediaporter.models.DownloadFile;

import java.util.List;

public interface DownloaderService {
    List<DownloadFile> downloads();
    void deleteDownloadFileById(Long id);
    DownloadFile createdDownloadFile(DownloadFileDto downloadFileDto);
    void downloadFile();
}
