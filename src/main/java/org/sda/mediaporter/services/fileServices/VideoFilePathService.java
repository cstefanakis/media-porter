package org.sda.mediaporter.services.fileServices;

import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.VideoFilePath;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public interface VideoFilePathService {
    VideoFilePath getVideoFilePathByPath(String videoFilePathFile);
    VideoFilePath getVideoFilePathById(Long videoPathFileId);
    VideoFilePath getVideoFilePath(Path videoFilePathFile);
    VideoFilePath createVideoFilePath(SourcePath sourcePath, Path videoFilePathFile);

    void deleteVideoFilePath(VideoFilePath videoFilePath);

    List<VideoFilePath> getAllVideoFilePaths();
}
