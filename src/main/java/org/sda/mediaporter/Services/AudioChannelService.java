package org.sda.mediaporter.Services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.AudioChannelDto;
import org.sda.mediaporter.models.metadata.AudioChannel;

import java.util.List;

public interface AudioChannelService {
    AudioChannel getAudioChannelByChannels(Integer channels);
    List<AudioChannel> getAllAudioChannels();
    AudioChannel getAudioChannelById(Long id);
    AudioChannel createAudioChannel(@Valid AudioChannelDto audioChannelDto);
    void deleteAudioChannelById(Long id);
    void updateAudioChannelById(Long id, AudioChannelDto audioChannelDto);
}
