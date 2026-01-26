package org.sda.mediaporter.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    private String videoCodec;
    private String videoResolution;
    private Integer videoBitrate;
}
