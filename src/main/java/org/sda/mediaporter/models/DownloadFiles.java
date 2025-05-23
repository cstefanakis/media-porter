package org.sda.mediaporter.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sda.mediaporter.models.enums.LibraryItems;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFiles {
    @Id
    private Long id;
    private String url;
    private String name;
    @Enumerated(EnumType.STRING)
    private LibraryItems libraryItems;
}
