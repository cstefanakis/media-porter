package org.sda.mediaporter.controllers;

import jakarta.persistence.metamodel.EntityType;
import org.sda.mediaporter.Servicies.FileDownloaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/file-downloads")
public class FileDownloaderController {

    private final FileDownloaderService fileDownloaderService;

    @Autowired
    public FileDownloaderController(FileDownloaderService fileDownloaderService) {
        this.fileDownloaderService = fileDownloaderService;
    }

    @GetMapping("{search}")
    public ResponseEntity <List<String>> getFiles(@PathVariable String search) {
        return ResponseEntity.ok(fileDownloaderService.getFiles(search));
    }
}
