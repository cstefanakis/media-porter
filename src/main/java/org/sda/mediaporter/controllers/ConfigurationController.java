package org.sda.mediaporter.controllers;


import org.sda.mediaporter.services.ConfigurationService;
import org.sda.mediaporter.dtos.ConfigurationDto;
import org.sda.mediaporter.models.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('LEVEL_USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Configuration> getConfigurationById(@PathVariable("id") Long id){
        Configuration configuration = configurationService.getConfigurationById(id);
        return ResponseEntity.ok().body(configuration);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Configuration> updateConfigurationById(@PathVariable("id") Long id,
                                                                 @RequestBody ConfigurationDto configurationDto){
        configurationService.updateConfiguration(id, configurationDto);
        Configuration configuration = configurationService.getConfigurationById(id);
        return ResponseEntity.ok().body(configuration);
    }

}
