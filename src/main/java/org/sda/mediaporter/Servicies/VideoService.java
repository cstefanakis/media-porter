package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Video;

import java.nio.file.Path;

public interface VideoService {
    Video createVideo(Video video);
    Video createVideoFromPath(Path file);
    Video updateMovieVideo(Long id, Video video, Movie movie);
}
