package org.sda.mediaporter.services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;

import java.util.List;

public interface ConfigurationService {
    Configuration getConfiguration();
    List<Genre> getGenresFromConfiguration(Configuration configuration);
    List<Resolution> getVideoResolutionFromConfiguration(Configuration configuration);
    List<Codec> getVideoCodecsFromConfiguration(Configuration configuration);
    List<Codec> getAudioCodecsFromConfiguration(Configuration configuration);
    List<AudioChannel> getAudioChannelsFromConfiguration(Configuration configuration);
    List<Language> getLanguagesFromConfiguration(@Valid Configuration configuration);
    void updateConfiguration(ConfigurationDto configurationDto);
}
