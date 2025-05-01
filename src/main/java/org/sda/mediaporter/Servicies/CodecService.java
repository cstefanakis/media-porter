package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.repositories.CodecRepository;

public interface CodecService {
    Codec getCodecById(Long id);
    Codec getCodecByName(String name);
    Codec createCodec(Codec codec);
}
