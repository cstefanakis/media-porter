package org.sda.mediaporter.controllers;

import org.apache.coyote.Response;
import org.sda.mediaporter.Services.ResolutionService;
import org.sda.mediaporter.models.metadata.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/by-name")
    public ResponseEntity<Resolution> getResolutionByName(@RequestParam("name") String name){
        Resolution resolution = resolutionService.getResolutionByName(name);
        return ResponseEntity.ok(resolution);
    }

    @GetMapping()
    public ResponseEntity<List<Resolution>> getAllResolutions(){
        List<Resolution> allResolutions = resolutionService.getAllResolutions();
        return ResponseEntity.ok(allResolutions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resolution> getResolutionById(@PathVariable("id") Long id){
        Resolution resolution = resolutionService.getResolutionById(id);
        return ResponseEntity.ok(resolution);
    }

    @PostMapping()
    public ResponseEntity<Resolution> createResolution(@RequestParam("name") String name){
        Resolution createdResolution = resolutionService.createResolution(name);
        return ResponseEntity.ok(createdResolution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateResolution(@PathVariable("id") Long id,
                                                 @RequestParam("name") String name){
        resolutionService.updateResolution(id, name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResolution(@PathVariable("id") Long id){
        resolutionService.deleteResolution(id);
        return ResponseEntity.noContent().build();
    }
}
