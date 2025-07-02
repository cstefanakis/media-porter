package org.sda.mediaporter.controllers;

import org.apache.coyote.Response;
import org.aspectj.apache.bcel.classfile.Code;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.dtos.CodecDto;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/name-type")
    public ResponseEntity<Codec> getCodecByNameAndMediaType(@RequestParam("name") String name,
                                                            @RequestParam("mediaType")MediaTypes mediaType){
        Codec codec = codecService.getCodecByNameAndMediaType(name, mediaType);
        return ResponseEntity.ok(codec);
    }

    @GetMapping("/media-type")
    public ResponseEntity<List<Codec>> getCodecsByMediaType(@RequestParam("mediaType") MediaTypes mediaType){
        List<Codec> codecs = codecService.getByMediaType(mediaType);
        return ResponseEntity.ok(codecs);
    }

    @GetMapping()
    public ResponseEntity<List<Codec>> getAllCodecs(){
        List<Codec> allCodecs = codecService.getAllCodecs();
        return ResponseEntity.ok(allCodecs);
    }

    @PostMapping()
    public ResponseEntity<Codec> createCodec(@RequestBody CodecDto codecDto){
        Codec createdCodec = codecService.createCodec(codecDto);
        return ResponseEntity.ok(createdCodec);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCodec(@PathVariable("id") Long id,
                                             @RequestBody CodecDto codecDto){
        codecService.updateCodec(id, codecDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodec(@PathVariable("id") Long id){
        codecService.deleteCodec(id);
        return ResponseEntity.noContent().build();
    }
}
