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
    @Column(name = "paths")
    private String path;

    @Column(name = "titles")
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "path_types")
    private PathType pathType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "library_items")
    private LibraryItems libraryItem;

    public enum PathType{
        DOWNLOAD, SOURCE, EXTERNAL
    }
}
