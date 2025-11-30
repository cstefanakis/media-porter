package org.sda.mediaporter.dtos;

import lombok.*;
import org.sda.mediaporter.models.TvShowEpisode;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TvShowUpdateDto {
    private LocalDateTime modificationDateTime;
    private TvShowEpisode tvShowEpisode;
}
