package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video_file_paths")
public class VideoFilePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    @NotEmpty
    private String filePath;

    @Column(name = "modification_date_time")
    private LocalDateTime modificationDateTime;

    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @OneToMany(mappedBy = "videoFilePath",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Audio> audios = new ArrayList<>();

    @OneToMany(mappedBy = "videoFilePath"
            ,fetch = FetchType.EAGER
            ,cascade = CascadeType.ALL
            ,orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Subtitle> subtitles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "source_path")
    @JsonManagedReference
    @NotNull
    private SourcePath sourcePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_show_episode_id")
    @JsonBackReference
    private TvShowEpisode tvShowEpisode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
