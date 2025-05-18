package org.sda.mediaporter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sda.mediaporter.models.enums.LibraryItems;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourcePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    private String title;
    @Enumerated(EnumType.STRING)
    private LibraryItems type;
}
