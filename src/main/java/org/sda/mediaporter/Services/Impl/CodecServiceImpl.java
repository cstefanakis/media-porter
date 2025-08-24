package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.dtos.CodecDto;
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
    public List<Codec> getByMediaType(MediaTypes mediaType) {
        return codecRepository.findByMediaType(mediaType);
    }


    @Override
    public Codec createCodec(CodecDto codecDto) {
        Optional<Codec> codecOptional = codecRepository.findByName(codecDto.getName());
        if(codecOptional.isEmpty()){
            return codecRepository.save(toEntity(new Codec(), codecDto));
        }
        throw new EntityExistsException(String.format("Codec with name %s already exist", codecDto.getName()));
    }

    @Override
    public Codec autoCreateCodec(String codecName, MediaTypes mediaType) {
        Optional<Codec> codec = codecRepository.findByName(codecName);
        return codec.orElseGet(() -> codecRepository.save(
                Codec.builder()
                        .name(codecName)
                        .mediaType(mediaType)
                        .build()));
    }

    @Override
    public void updateCodec(Long codecId, CodecDto codecDto) {
        Optional<Codec> codecOptional = codecRepository.findById(codecId);
        if(codecOptional.isPresent()){
            codecRepository.save(toEntity(codecOptional.get(), codecDto));
        }else{
            throw new EntityNotFoundException(String.format("Codec with id %s not found", codecId));
        }
    }

    @Override
    public void deleteCodec(Long codecId) {
        Codec codec = getCodecById(codecId);
        codecRepository.delete(codec);
    }

    private Codec toEntity(Codec codec, CodecDto codecDto){
        codec.setName(validatedCodecName(codec, codecDto));
        codec.setMediaType(validatedMediaType(codec, codecDto));
        return codec;
    }

    private String validatedCodecName(Codec codec, CodecDto codecDto){
        String nameDto = codecDto.getName();
        String name = codec.getName();
        if(nameDto == null && name != null){
            return name;
        }
        if(nameDto != null && nameDto.equals(name)){
            return nameDto;
        }
        Optional<Codec> codecOptional = codecRepository.findByName(nameDto);
        if(codecOptional.isEmpty()){
            return nameDto;
        }
        throw new EntityExistsException(String.format("Codec with name %s already exist", nameDto));
    }

    private MediaTypes validatedMediaType(Codec codec, CodecDto codecDto){
        MediaTypes mediaTypeDto = codecDto.getMediaType();
        MediaTypes mediaType = codec.getMediaType();
        if(mediaTypeDto == null && mediaType != null){
            return mediaType;
        }
        return mediaTypeDto;
    }
}