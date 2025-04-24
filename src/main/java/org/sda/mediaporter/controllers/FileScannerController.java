package org.sda.mediaporter.controllers;

import org.sda.mediaporter.Servicies.FileScannerService;
import org.sda.mediaporter.models.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/file-scanner")
public class FileScannerController {
    private final FileScannerService fileScannerService;

    public FileScannerController(FileScannerService fileScannerService) {
        this.fileScannerService = fileScannerService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getScannedMovies (@RequestParam String path) {
        List<Movie> movies =  fileScannerService.scannedMoviesPath(path);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/files")
    public ResponseEntity<List<Path>> getScannedFiles (@RequestParam String path) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileScannerService.files(path));
    }
}
