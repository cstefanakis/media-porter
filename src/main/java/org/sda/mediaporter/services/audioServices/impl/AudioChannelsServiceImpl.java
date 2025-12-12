package org.sda.mediaporter.services.audioServices.impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.services.audioServices.AudioChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class AudioChannelsServiceImpl implements AudioChannelService {

    private final AudioChannelRepository audioChannelRepository;

    @Autowired
    public AudioChannelsServiceImpl(AudioChannelRepository audioChannelRepository) {
        this.audioChannelRepository = audioChannelRepository;
    }

    @Override
    public AudioChannel getAudioChannelByChannels(Integer channels) {
        return audioChannelRepository.findAudioChannelByChannel(channels).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Audio channels with channels %s not found", channels))
        );
    }

    @Override
    public List<AudioChannel> getAllAudioChannels() {
        return audioChannelRepository.findAll();
    }

    @Override
    public AudioChannel getAudioChannelById(Long id) {
        return audioChannelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Audio channel with id %s not found", id))
        );
    }

    @Override
    public AudioChannel getAudioChannelByChannelsOrNull(Integer channels) {
        Optional<AudioChannel> audioChannelOptional = audioChannelRepository.findAudioChannelByChannel(channels);
        return audioChannelOptional.orElse(null);
    }
}
