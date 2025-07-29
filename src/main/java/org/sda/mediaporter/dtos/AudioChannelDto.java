package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AudioChannelDto {

    @NotEmpty(message = "Title must not be null or empty")
    String title;

    @NotNull(message = "Channels must not be null")
    Integer channels;

    String description;
}
