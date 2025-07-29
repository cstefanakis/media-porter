package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResolutionDto {

    @NotEmpty(message = "Name must not be empty")
    private String name;
}
