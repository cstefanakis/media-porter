package org.sda.mediaporter.models;

import com.drew.lang.annotations.NotNull;
import jakarta.persistence.*;
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
public class DownloadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(length = 2048)
    private String url;
    private String name;
    @Enumerated(EnumType.STRING)
    private LibraryItems libraryItems;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        IN_PROGRESS, DOWNLOADED, FAILED, UN_DOWNLOADED
    }
}
