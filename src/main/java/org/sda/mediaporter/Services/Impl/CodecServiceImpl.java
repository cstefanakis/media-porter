package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.http.parser.MediaType;
import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.dtos.CodecDto;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.enums.Codecs;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodecServiceImpl implements CodecService {

    private final CodecRepository codecRepository;

    @Autowired
    public CodecServiceImpl(CodecRepository codecRepository) {
        this.codecRepository = codecRepository;
    }


    @Override
    public Codec getCodecByNameAndMediaType(String codecName, MediaTypes mediaType) {
        return codecRepository.findByNameAndMediaType(codecName, mediaType).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Codec not found with name %s and mediaType: %s", codecName, mediaType)));
    }

    @Override
    public Codec getCodecById(Long id) {
        return codecRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Codec with id %s not found", id)));
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
        Optional<Codec> codecFromDb = codecRepository.findById(codecId);
        codecFromDb.ifPresent(codecRepository::delete);
        throw new EntityNotFoundException(String.format("Codec with id %s not found",  codecId));
    }

    private Codec toEntity(Codec codec, CodecDto codecDto){
        codec.setName(codecDto.getName() == null? codec.getName() : codecDto.getName());
        codec.setMediaType(codecDto.getMediaType() == null? codec.getMediaType() : codecDto.getMediaType());
        return codec;
    }
}