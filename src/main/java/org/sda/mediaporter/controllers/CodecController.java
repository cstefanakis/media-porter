package org.sda.mediaporter.controllers;

import org.sda.mediaporter.services.CodecService;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/codecs")
public class CodecController {
    private final CodecService codecService;

    @Autowired
    public CodecController(CodecService codecService) {
        this.codecService = codecService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-name-and-media-type")
    public ResponseEntity<Codec> getCodecByNameAndMediaType(@RequestParam("name") String name,
                                                            @RequestParam("mediaType")MediaTypes mediaType){
        Codec codec = codecService.getCodecByNameAndMediaType(name, mediaType);
        return ResponseEntity.ok(codec);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-media-type")
    public ResponseEntity<List<Codec>> getCodecsByMediaType(@RequestParam("mediaType") MediaTypes mediaType){
        List<Codec> codecs = codecService.getCodecsByMediaType(mediaType);
        return ResponseEntity.ok(codecs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Codec> getCodecById(@PathVariable("id") Long id){
        Codec codec = codecService.getCodecById(id);
        return ResponseEntity.ok(codec);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<Codec>> getAllCodecs(){
        List<Codec> allCodecs = codecService.getAllCodecs();
        return ResponseEntity.ok(allCodecs);
    }
}
