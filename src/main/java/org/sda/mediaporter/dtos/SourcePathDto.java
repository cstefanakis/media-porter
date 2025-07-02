package org.sda.mediaporter.dtos;

import lombok.Getter;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;

@Getter
public class SourcePathDto {
    private String path;
    private String title;
    private SourcePath.Type pathType;
    private LibraryItems type;
}
