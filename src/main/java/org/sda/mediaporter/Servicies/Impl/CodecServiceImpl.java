package org.sda.mediaporter.Servicies.Impl;

import jakarta.persistence.EntityExistsException;
import org.sda.mediaporter.Servicies.CodecService;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.repositories.CodecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CodecServiceImpl implements CodecService {

    private final CodecRepository codecRepository;

    @Autowired
    public CodecServiceImpl(CodecRepository codecRepository) {
        this.codecRepository = codecRepository;
    }


    @Override
    public Codec getCodecById(Long id) {
        return codecRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Codec found with id: " + id));
    }

    @Override
    public Codec getCodecByName(String name) {
        return codecRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("No Codec found with name: " + name));
    }

    @Override
    public Codec createCodec(Codec codec) {
        return codecRepository.save(validateCodec(codec));
    }

    //validated codec by name
    private Codec validateCodec(Codec codec) {
        Optional<Codec> codecOptional = codecRepository.findByName(codec.getName());
        if(codecOptional.isEmpty()){
            return codec;
        }throw new EntityExistsException("Codec with name: " + codec.getName() + " already exists");
    }
}
