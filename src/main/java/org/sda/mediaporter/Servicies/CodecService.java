package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.metadata.Codec;

public interface CodecService {
    Codec autoCreateCodec(String codecName);
}
