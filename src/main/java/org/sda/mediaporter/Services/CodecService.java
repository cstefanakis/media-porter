package org.sda.mediaporter.Services;

import jakarta.validation.Valid;
import org.sda.mediaporter.dtos.CodecDto;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;

import java.util.List;

public interface CodecService {
    Codec getCodecByNameAndMediaType(String codecName, MediaTypes mediaType);
    Codec getCodecById(Long id);
    Codec getCodecByName(String name);
    List<Codec> getAllCodecs();
    List<Codec> getByMediaType(MediaTypes mediaType);
    Codec createCodec(@Valid CodecDto codecDto);
    Codec autoCreateCodec(String codecName, MediaTypes mediaType);
    void updateCodec(Long codecId, CodecDto codecDto);
    void deleteCodec(Long codecId);
}
