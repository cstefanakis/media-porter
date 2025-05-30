package org.sda.mediaporter.Services;

import org.sda.mediaporter.models.metadata.Codec;

public interface CodecService {
    Codec autoCreateCodec(String codecName);
}
