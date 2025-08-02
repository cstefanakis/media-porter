package org.sda.mediaporter.controllers;

import jakarta.validation.Valid;
import org.sda.mediaporter.Services.DownloaderService;
import org.sda.mediaporter.dtos.DownloadFileDto;
import org.sda.mediaporter.models.DownloadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/downloader")

public class DownloaderController {

    private final DownloaderService downloaderService;

    @Autowired
    public DownloaderController(DownloaderService downloaderService) {
        this.downloaderService = downloaderService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<DownloadFile>> downloads(){
        List<DownloadFile> allFiles = downloaderService.downloads();
        return ResponseEntity.ok(allFiles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/downloaded")
    public ResponseEntity<List<DownloadFile>> getDownloadedFiles() {
        return ResponseEntity.ok(List.of(new DownloadFile()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/in-progress")
    public ResponseEntity<List<DownloadFile>> getUnDownloadedFilesInProgress() {
        return ResponseEntity.ok(List.of(new DownloadFile()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-file")
    public ResponseEntity<DownloadFile> addDownloadedFile(@RequestBody @Valid DownloadFileDto downloadFileDto) {
            DownloadFile createdDownloadFile = downloaderService.createdDownloadFile(downloadFileDto);
        return ResponseEntity.ok().body(createdDownloadFile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDownloadedFile(@PathVariable Long id) {
        downloaderService.deleteDownloadFileById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DownloadFile> updateDownloadedFile(@PathVariable int id, @RequestBody DownloadFileDto downloadFileDto) {
        return ResponseEntity.ok().body(new DownloadFile());
    }
}
