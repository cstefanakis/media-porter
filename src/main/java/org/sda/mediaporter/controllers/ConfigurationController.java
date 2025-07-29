package org.sda.mediaporter.controllers;


import org.sda.mediaporter.Services.ConfigurationService;
import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.models.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/configuration")
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
        configurationService.updateConfiguration(configurationDto);
        return ResponseEntity.ok().build();
    }

}
