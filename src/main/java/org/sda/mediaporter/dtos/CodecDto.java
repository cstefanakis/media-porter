package org.sda.mediaporter.dtos;


import com.drew.lang.annotations.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.sda.mediaporter.models.enums.MediaTypes;

@Getter
public class CodecDto {
    private String name;
    private MediaTypes mediaType;
}
