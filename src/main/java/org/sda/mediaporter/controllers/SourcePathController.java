package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Services.SourcePathService;
import org.sda.mediaporter.dtos.SourcePathDto;
import org.sda.mediaporter.models.SourcePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sources")

public class SourcePathController {

    private final SourcePathService sourcePathService;

    @Autowired
    public SourcePathController(SourcePathService sourcePathService) {
        this.sourcePathService = sourcePathService;
    }

    @GetMapping()
    public ResponseEntity<List<SourcePath>> getSourcePaths() {
        List<SourcePath> sourcePaths = sourcePathService.getSourcePaths();
        return new ResponseEntity<>(sourcePaths, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SourcePath> getById(@PathVariable Long id) {
        SourcePath sourcePath = sourcePathService.getById(id);
        return new ResponseEntity<>(sourcePath, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<SourcePath> createSourcePath(@RequestBody SourcePathDto sourcePathDto) {
        SourcePath createdSourcePath = sourcePathService.createSourcePath(sourcePathDto);
        return new ResponseEntity<>(createdSourcePath, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SourcePath> updateSourcePath(@PathVariable Long id, @RequestBody SourcePathDto sourcePathDto) {
        SourcePath updatedSourcePath = sourcePathService.updateSourcePath(id, sourcePathDto);
        return new ResponseEntity<>(updatedSourcePath, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSourcePath(@PathVariable Long id) {
        sourcePathService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
