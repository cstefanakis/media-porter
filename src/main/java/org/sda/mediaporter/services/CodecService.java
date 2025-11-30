package org.sda.mediaporter.services;

import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;

import java.util.List;

public interface CodecService {
    Codec getCodecByNameAndMediaType(String codecName, MediaTypes mediaType);
    Codec getCodecById(Long id);
    Codec getCodecByName(String name);
    List<Codec> getAllCodecs();
    List<Codec> getCodecsByMediaType(MediaTypes mediaType);
    Codec getOrCreateCodecByCodecNameAndMediaType(String codecName, MediaTypes mediaTypes);
}
