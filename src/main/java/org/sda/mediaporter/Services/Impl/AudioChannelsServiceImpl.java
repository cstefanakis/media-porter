package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.AudioChannelService;
import org.sda.mediaporter.dtos.AudioChannelDto;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
    public AudioChannel createAudioChannel(AudioChannelDto audioChannelDto) {
        AudioChannel audioChannel = new AudioChannel();
        audioChannel.setTitle(validatedTitle(audioChannel, audioChannelDto));
        audioChannel.setChannels(validatedChannels(audioChannel, audioChannelDto));
        audioChannel.setDescription(validatedDescription(audioChannel, audioChannelDto.getDescription()));
        System.out.println(audioChannel.getTitle() + " - " + audioChannelDto.getTitle());
        return audioChannelRepository.save(audioChannel);
    }

    @Override
    public void deleteAudioChannelById(Long id) {
        AudioChannel audioChannel = getAudioChannelById(id);
        audioChannelRepository.delete(audioChannel);
    }

    @Override
    public void updateAudioChannelById(Long id, AudioChannelDto audioChannelDto) {
        AudioChannel audioChannel = getAudioChannelById(id);
        audioChannel.setTitle(validatedTitle(audioChannel, audioChannelDto));
        audioChannel.setChannels(validatedChannels(audioChannel, audioChannelDto));
        audioChannel.setDescription(validatedDescription(audioChannel, audioChannel.getDescription()));
        audioChannelRepository.save(audioChannel);
    }

    private String validatedTitle(AudioChannel audioChannel, AudioChannelDto audioChannelDto){
        String titleDto = audioChannelDto.getTitle();
        String title = audioChannel.getTitle();

        if(titleDto == null && title != null) {
            return title;
        }

        if(title != null && title.equals(titleDto)){
            return titleDto;
        }

        Optional<AudioChannel> audioChannelOptional = audioChannelRepository.findAudioChannelByTitle(titleDto);
        if(audioChannelOptional.isEmpty()){
            return titleDto;
        }

        throw new EntityExistsException(String.format("Audio channel with title %s already exist", title));
    }

    private Integer validatedChannels(AudioChannel audioChannel, AudioChannelDto audioChannelDto){

        Integer channelsDto = audioChannelDto.getChannels();
        Integer channels = audioChannel.getChannels();
        if(channelsDto == null && channels != null){
            return channels;
        }

        if(channelsDto != null && channelsDto.equals(channels)){
            return channelsDto;
        }
        Optional<AudioChannel> audioChannelOptional = audioChannelRepository.findAudioChannelByChannel(channelsDto);
        if(audioChannelOptional.isEmpty()){
            return channelsDto;
        }
        throw new EntityExistsException(String.format("Audio channels with channels %s already exist",channels));
    }

    private String validatedDescription(AudioChannel audioChannel, String description){
        if(description == null){
            return audioChannel.getDescription();
        }
        return description;
    }
}
