package org.sda.mediaporter.controllers;

import jakarta.validation.Valid;
import org.sda.mediaporter.Services.AudioChannelService;
import org.sda.mediaporter.dtos.AudioChannelDto;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public ResponseEntity<List<AudioChannel>> getAllAudioChannels(){
        List<AudioChannel> audioChannels = audioChannelService.getAllAudioChannels();
        return ResponseEntity.ok(audioChannels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudioChannel> getAudioChannelById(@PathVariable("id") Long id){
        AudioChannel audioChannel = audioChannelService.getAudioChannelById(id);
        return ResponseEntity.ok(audioChannel);
    }

    @GetMapping("/by-channels")
    public ResponseEntity<AudioChannel> getAudioChannelByChannels(@RequestParam("channels") Integer channels){
        AudioChannel audioChannel = audioChannelService.getAudioChannelByChannels(channels);
        return ResponseEntity.ok(audioChannel);
    }

    @PostMapping()
    public ResponseEntity<AudioChannel> createAudioChannel(@RequestBody @Valid AudioChannelDto audioChannelDto){
        AudioChannel audioChannel = audioChannelService.createAudioChannel(audioChannelDto);
        return ResponseEntity.ok(audioChannel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAudioChannelById(@PathVariable("id") Long id,
                                                       @RequestBody AudioChannelDto audioChannelDto){
        audioChannelService.updateAudioChannelById(id, audioChannelDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAudioChannelById(@PathVariable ("id") Long id){
        audioChannelService.deleteAudioChannelById(id);
        return ResponseEntity.noContent().build();
    }


}
