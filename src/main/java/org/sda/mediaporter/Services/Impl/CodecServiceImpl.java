package org.sda.mediaporter.Services.Impl;

import org.sda.mediaporter.Services.CodecService;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.enums.Codecs;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
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
    public Codec autoCreateCodec(String codecName) {
        Optional<Codec> codecOptional = codecRepository.findByName(codecName);
        if (codecOptional.isEmpty()) {
            Codec generatedCodec = getCodecFromEnum(codecName);
            if (generatedCodec != null) {
                return codecRepository.save(generatedCodec);
            }
        } else {
            return codecOptional.get();
        }
        return null;
    }

    private Codec getCodecFromEnum(String codecName){
        for(Codecs codec : Codecs.values()){
            if(codecName.equalsIgnoreCase(codec.getCodecName()) ||
                    codecName.equalsIgnoreCase(codec.name())){
                return Codec.builder()
                        .name(codec.getCodecName())
                        .mediaType(codec.getMediaTypes())
                        .build();
            }
        }return null;
    }
}