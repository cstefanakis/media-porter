package org.sda.mediaporter.models.multimedia.audioFiles;

import jakarta.persistence.*;
import org.sda.mediaporter.models.Contributor;

import java.util.List;

@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @ManyToMany
    @JoinTable(
            name = "artists",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Contributor> artists;
    private String coverPath;
}
