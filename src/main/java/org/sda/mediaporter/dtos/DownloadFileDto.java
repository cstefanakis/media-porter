package org.sda.mediaporter.dtos;

import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sda.mediaporter.models.DownloadFile;
import org.sda.mediaporter.models.enums.LibraryItems;

@Getter
@NoArgsConstructor
public class DownloadFileDto {
    private String url;
    private String name;
    private LibraryItems libraryItem;
    private DownloadFile.Status status;
}
