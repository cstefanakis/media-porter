package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;

@Getter
@Builder
public class SourcePathDto {
    @NotEmpty(message = "Path must not be empty")
    private String path;

    @NotNull(message = "Path type must not be null")
    private String title;

    @NotNull(message = "Path type item must not be null")
    private SourcePath.PathType pathType;

    @NotNull(message = "Library item must not be null")
    private LibraryItems libraryItem;
}
