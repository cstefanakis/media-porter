package org.sda.mediaporter.controllers;

import jakarta.validation.Valid;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.dtos.CodecDto;
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
        List<Codec> codecs = codecService.getByMediaType(mediaType);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Codec> createCodec(@RequestBody @Valid CodecDto codecDto){
        Codec createdCodec = codecService.createCodec(codecDto);
        return ResponseEntity.ok(createdCodec);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCodec(@PathVariable("id") Long id,
                                             @RequestBody CodecDto codecDto){
        codecService.updateCodec(id, codecDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodec(@PathVariable("id") Long id){
        codecService.deleteCodec(id);
        return ResponseEntity.noContent().build();
    }
}
