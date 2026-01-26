package org.sda.mediaporter.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AudioDto {
    private String audioCodec;
    private Integer audioChannel;
    private Integer audioBitrate;
    private String audioLanguage;
}
