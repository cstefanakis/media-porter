package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;

import java.nio.file.Path;
import java.util.List;

public interface AudioService {
    List<Audio> createAudioListFromFile(Path file, Movie movie);
    List<Audio> getAudioListFromFile(Path file);
    Codec getAudioCodec(String[] audioProperties);
    AudioChannel getAudioChannel(String[] audioProperties);
    Integer getAudioBitrate(String[] audioProperties);
    Language getAudioLanguageByCode(String[] audioProperties);

}
