package org.sda.mediaporter.Services;

import org.sda.mediaporter.dtos.AudioChannelDto;
import org.sda.mediaporter.models.metadata.AudioChannel;

import java.util.List;

public interface AudioChannelService {
    AudioChannel getAudioChannelByChannels(Integer channels);
    List<AudioChannel> getAllAudioChannels();
    AudioChannel getAudioChannelById(Long id);
    AudioChannel createAudioChannel(AudioChannelDto audioChannelDto);
    void deleteAudioChannelById(Long id);
    void updateAudioChannelById(Long id, AudioChannelDto audioChannelDto);
}
