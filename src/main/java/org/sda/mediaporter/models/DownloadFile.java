package org.sda.mediaporter.models;

import com.drew.lang.annotations.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sda.mediaporter.models.enums.LibraryItems;

@Entity
@Table(name ="download_files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "urls", length = 2048)
    private String url;

    @Column(name = "names")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "library_item")
    private LibraryItems libraryItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "statuses")
    private Status status;

    public enum Status {
        IN_PROGRESS, DOWNLOADED, FAILED, UN_DOWNLOADED
    }
}
