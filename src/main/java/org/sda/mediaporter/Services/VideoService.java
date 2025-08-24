package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Video;

import java.nio.file.Path;

public interface VideoService {
    Video createVideoFromPath(Path file, Movie movie);
    String getCodecFromFilePathViFFMpeg(Path file);
    String getResolutionFromFilePathViFFMpeg(Path file);
    Integer getBitrateFromFilePathViFFMpeg(Path file);
    Video getVideoFromPath(Path file);
}
