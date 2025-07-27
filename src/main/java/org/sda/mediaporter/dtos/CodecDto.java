package org.sda.mediaporter.dtos;

import lombok.Builder;
import lombok.Getter;
import org.sda.mediaporter.models.enums.MediaTypes;

@Getter
@Builder
public class CodecDto {
    private String name;
    private MediaTypes mediaType;
}
