package org.sda.mediaporter.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AudioChannelDto {
    String title;
    Integer channels;
    String description;
}
