package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VideoFilePathDto {
    @NotBlank
    private String filePath;
    private Long videoId;
    private List<Long> audiosId;
    private List<Long> subtitlesId;
}
