package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Services.AudioChannelService;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audio-channels")
public class AudioChannelController {
    private final AudioChannelService audioChannelService;

    @Autowired
    public AudioChannelController(AudioChannelService audioChannelService) {
        this.audioChannelService = audioChannelService;
    }

    @GetMapping("/")
    public ResponseEntity<List<AudioChannel>> getAllAudioChannels(){
        List<AudioChannel> audioChannels = audioChannelService.getAllAudioChannels();
        return ResponseEntity.ok(audioChannels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudioChannel> getAudioChannelById(@PathVariable("id") Long id){
        AudioChannel audioChannel = audioChannelService.getAudioChannelById(id);
        return ResponseEntity.ok(audioChannel);
    }
}
