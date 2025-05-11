package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Audio;

import java.nio.file.Path;
import java.util.List;

public interface AudioService {
    Audio createAudio(Audio audio);
    List<Audio> createAudioListFromFile(Path file);
    Audio updateMovieAudio(Long id, Audio audio, Movie movie);
}
