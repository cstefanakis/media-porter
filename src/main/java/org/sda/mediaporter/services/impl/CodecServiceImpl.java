package org.sda.mediaporter.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.services.CodecService;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.List;
import java.util.Optional;

@Service
@Validated
public class CodecServiceImpl implements CodecService {

    private final CodecRepository codecRepository;

    @Autowired
    public CodecServiceImpl(CodecRepository codecRepository) {
        this.codecRepository = codecRepository;
    }


    @Override
    public Codec getCodecByNameAndMediaType(String codecName, MediaTypes mediaType) {
        return codecRepository.findByNameAndMediaType(codecName, mediaType)
                .orElseThrow(
                        ()-> new EntityNotFoundException(String.format("Codec not found with name %s and mediaType: %s", codecName, mediaType))
                );
    }

    @Override
    public Codec getCodecById(Long id) {
        return codecRepository.findById(id)
                .orElseThrow(
                        ()-> new EntityNotFoundException(String.format("Codec with id %s not found", id))
                );
    }

    @Override
    public Codec getCodecByName(String codecName) {
        return codecRepository.findByName(codecName)
                .orElseThrow(
                        ()-> new EntityNotFoundException(String.format("Codec with name: %s not exist", codecName))
                );
    }

    @Override
    public List<Codec> getAllCodecs() {
        return codecRepository.findAll();
    }

    @Override
    public List<Codec> getCodecsByMediaType(MediaTypes mediaType) {
        return codecRepository.findByMediaType(mediaType);
    }

    @Override
    public Codec getOrCreateCodecByCodecNameAndMediaType(String codecName, MediaTypes mediaTypes) {
        Optional<Codec> codecOptional = codecRepository.findByNameAndMediaType(codecName, mediaTypes);
        return codecOptional.orElseGet(() ->
                codecRepository.save(Codec.builder()
                                .name(codecName)
                                .mediaType(mediaTypes)
                        .build()));
    }
}