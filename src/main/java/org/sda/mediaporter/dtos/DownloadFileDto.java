package org.sda.mediaporter.dtos;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sda.mediaporter.models.enums.LibraryItems;

@Setter
@NoArgsConstructor
public class DownloadFileDto {
    private String url;
    private String name;
    private LibraryItems libraryItem;
}
