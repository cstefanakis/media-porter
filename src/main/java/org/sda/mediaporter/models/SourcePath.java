package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.enums.LibraryItems;

import java.util.List;

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

    @Column(name = "paths", nullable = false)
    private String path;

    @Column(name = "titles")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "path_types", nullable = false)
    private PathType pathType;

    @Enumerated(EnumType.STRING)
    @Column(name = "library_items", nullable = false)
    private LibraryItems libraryItem;

    public enum PathType {
        DOWNLOAD, SOURCE, EXTERNAL, TV_RECORDS
    }

    @OneToMany(mappedBy = "sourcePath")
    @JsonBackReference
    private List<VideoFilePath> videoFilePaths;

    @OneToOne
    @JoinColumn(name = "configuration_id")
    @JsonManagedReference
    private Configuration configuration;
}
