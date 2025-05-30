package org.sda.mediaporter.controllers;

import org.sda.mediaporter.models.DownloadFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/downloader")
@CrossOrigin(origins = "http://localhost:5173")
public class DownloaderController {

    @GetMapping("/downloaded")
    public ResponseEntity<List<DownloadFile>> getDownloadedFiles() {
        return ResponseEntity.ok(List.of(new DownloadFile()));
    }

    @GetMapping("/in-progress")
    public ResponseEntity<List<DownloadFile>> getUnDownloadedFilesInProgress() {
        return ResponseEntity.ok(List.of(new DownloadFile()));
    }

    @PostMapping("/add-file")
    public ResponseEntity<DownloadFile> addDownloadedFile(
            @RequestBody DownloadFile downloadFile) {
        return ResponseEntity.ok().body(new DownloadFile());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DownloadFile> deleteDownloadedFile(@PathVariable int id) {
        return ResponseEntity.ok().body(new DownloadFile());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DownloadFile> updateDownloadedFile(@PathVariable int id, @RequestBody DownloadFile downloadFile) {
        return ResponseEntity.ok().body(new DownloadFile());
    }
}
