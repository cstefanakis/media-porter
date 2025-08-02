package org.sda.mediaporter.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.sda.mediaporter.Services.ResolutionService;
import org.sda.mediaporter.dtos.ResolutionDto;
import org.sda.mediaporter.models.metadata.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/resolutions")
public class ResolutionController {
    private final ResolutionService resolutionService;

    @Autowired
    public ResolutionController(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-name")
    public ResponseEntity<Resolution> getResolutionByName(@NotEmpty(message = "Name must not be empty") @RequestParam("name") String name){
        Resolution resolution = resolutionService.getResolutionByName(name);
        return ResponseEntity.ok(resolution);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<Resolution>> getAllResolutions(){
        List<Resolution> allResolutions = resolutionService.getAllResolutions();
        return ResponseEntity.ok(allResolutions);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Resolution> getResolutionById(@PathVariable("id") Long id){
        Resolution resolution = resolutionService.getResolutionById(id);
        return ResponseEntity.ok(resolution);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Resolution> createResolution(@RequestBody @Valid ResolutionDto resolutionDto){
        Resolution createdResolution = resolutionService.createResolution(resolutionDto);
        return ResponseEntity.ok(createdResolution);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateResolution(@PathVariable("id") Long id,
                                                 @RequestBody ResolutionDto resolutionDto){
        resolutionService.updateResolution(id, resolutionDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResolution(@PathVariable("id") Long id){
        resolutionService.deleteResolution(id);
        return ResponseEntity.noContent().build();
    }
}
