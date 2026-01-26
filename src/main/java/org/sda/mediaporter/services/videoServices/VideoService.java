package org.sda.mediaporter.services.videoServices;

import org.sda.mediaporter.dtos.VideoDto;
import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.metadata.Video;

import java.nio.file.Path;

public interface VideoService {
    Video createVideoFromPath(Path file, VideoFilePath videoFilePath);
    Video getVideoById(Long videoId);
    Video getVideoFromPath(Path file, VideoFilePath videoFilePath);
    void deleteVideoById(Long videoId);
    VideoDto getVideoDetails(Path file);
}
