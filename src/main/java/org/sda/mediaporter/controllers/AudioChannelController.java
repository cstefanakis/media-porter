package org.sda.mediaporter.controllers;

import org.sda.mediaporter.services.audioServices.AudioChannelService;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audio-channels")
public class AudioChannelController {
    private final AudioChannelService audioChannelService;

    @Autowired
    public AudioChannelController(AudioChannelService audioChannelService) {
        this.audioChannelService = audioChannelService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<AudioChannel>> getAllAudioChannels(){
        List<AudioChannel> audioChannels = audioChannelService.getAllAudioChannels();
        return ResponseEntity.ok(audioChannels);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AudioChannel> getAudioChannelById(@PathVariable("id") Long id){
        AudioChannel audioChannel = audioChannelService.getAudioChannelById(id);
        return ResponseEntity.ok(audioChannel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-channels")
    public ResponseEntity<AudioChannel> getAudioChannelByChannels(@RequestParam("channels") Integer channels){
        AudioChannel audioChannel = audioChannelService.getAudioChannelByChannels(channels);
        return ResponseEntity.ok(audioChannel);
    }
}
