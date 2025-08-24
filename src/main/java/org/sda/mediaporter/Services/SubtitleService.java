package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Subtitle;

import java.nio.file.Path;
import java.util.List;

public interface SubtitleService {
    List<Subtitle> createSubtitleListFromFile(Path file, Movie movie);
}
