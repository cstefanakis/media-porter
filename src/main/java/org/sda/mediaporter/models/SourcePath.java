package org.sda.mediaporter.models;

import jakarta.persistence.*;
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
    @NonNull
    private String path;
    private String title;
    @NonNull
    @Enumerated(EnumType.STRING)
    private Type pathType;
    @NonNull
    @Enumerated(EnumType.STRING)
    private LibraryItems type;

    public enum Type{
        DOWNLOAD, SOURCE, EXTERNAL
    }
}
