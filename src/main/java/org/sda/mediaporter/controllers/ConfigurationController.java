package org.sda.mediaporter.controllers;


import org.sda.mediaporter.Services.ConfigurationService;
import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.models.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/configuration")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://192.168.0.10:5173",
        "http://192.168.192.131:5173"
})
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @Autowired
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping()
    public ResponseEntity<Configuration> getConfiguration(){
        Configuration configuration = configurationService.getConfiguration();
        return ResponseEntity.ok().body(configuration);
    }

    @PutMapping()
    public ResponseEntity<Void> updateConfiguration(@RequestBody ConfigurationDto configurationDto){
        System.out.println("String genres: " + configurationDto.getGenresPrefer());
        System.out.println("String video codecs: " + configurationDto.getVideoCodecsPrefer());
        System.out.println("String audio codecs: " + configurationDto.getAudioCodecsPrefer());
        configurationService.updateConfiguration(configurationDto);
        return ResponseEntity.ok().build();
    }

}
