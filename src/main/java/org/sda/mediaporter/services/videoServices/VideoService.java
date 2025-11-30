package org.sda.mediaporter.services.videoServices;

import org.sda.mediaporter.models.metadata.Video;

import java.nio.file.Path;

public interface VideoService {
    Video createVideoFromPath(Path file);
    Video getVideoById(Long videoId);
    String getCodecFromFilePathViFFMpeg(Path file);
    String getResolutionFromFilePathViFFMpeg(Path file);
    Integer getBitrateFromFilePathViFFMpeg(Path file);
    Video getVideoFromPath(Path file);
}
