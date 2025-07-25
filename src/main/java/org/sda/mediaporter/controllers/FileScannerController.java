package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Services.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/file-scanner")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://192.168.0.10:5173",
        "http://192.168.192.131:5173"
})

public class FileScannerController {
    private final FileService fileScannerService;

    public FileScannerController(FileService fileScannerService) {
        this.fileScannerService = fileScannerService;
    }

    @GetMapping("/files")
    public ResponseEntity<List<Path>> getScannedFiles (@RequestParam String path) {
        Path filePath = Path.of(path);
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileScannerService.getVideoFiles(filePath));
    }
}
