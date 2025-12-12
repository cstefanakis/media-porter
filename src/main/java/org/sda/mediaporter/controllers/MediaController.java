package org.sda.mediaporter.controllers;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/media")

public class MediaController {
    @GetMapping("/video")
    public ResponseEntity<Resource> getVideo(@RequestParam String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        UrlResource video = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(file.getName()).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(video);
    }
}
