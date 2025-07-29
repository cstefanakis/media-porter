package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContributorDto {

    @NotEmpty(message = "Full name must not be empty")
    private String fullName;
}
