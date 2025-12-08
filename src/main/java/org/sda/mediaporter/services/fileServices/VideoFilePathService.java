package org.sda.mediaporter.services.fileServices;

import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.VideoFilePath;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public interface VideoFilePathService {
    VideoFilePath getVideoFilePathByPath(String videoFilePathFile);
    VideoFilePath getVideoFilePathById(Long videoPathFileId);
    VideoFilePath getVideoFilePath(Path videoFilePathFile);
    VideoFilePath createVideoFilePath(Path videoFilePathFile);
    VideoFilePath updateSourcePathFileAndPath(VideoFilePath videoFilePath, Path filePath);
    void deleteVideoFilePath(VideoFilePath videoFilePath);
    List<VideoFilePath> getAllVideoFilePaths();
    boolean isVideoFilePathWithPathExist(String filePath);
    void addMovie(Movie movie, VideoFilePath videoFilePath);
}
