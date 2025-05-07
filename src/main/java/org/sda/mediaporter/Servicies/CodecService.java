package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.repositories.CodecRepository;

public interface CodecService {
    Codec autoCreateCodec(String codecName);
}
