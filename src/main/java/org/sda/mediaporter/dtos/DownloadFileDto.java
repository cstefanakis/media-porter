package org.sda.mediaporter.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.DownloadFile;
import org.sda.mediaporter.models.enums.LibraryItems;

@Getter
@NoArgsConstructor
public class DownloadFileDto {

    @NotBlank(message = "url must not be blank")
    private String url;

    private String name;

    private LibraryItems libraryItem;

    private DownloadFile.Status status;
}
