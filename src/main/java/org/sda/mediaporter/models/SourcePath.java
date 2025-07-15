package org.sda.mediaporter.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.sda.mediaporter.models.enums.LibraryItems;

@Entity
@Table(name = "source_paths")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SourcePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String path;
    private String title;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PathType pathType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private LibraryItems libraryItem;

    public enum PathType{
        DOWNLOAD, SOURCE, EXTERNAL
    }
}
