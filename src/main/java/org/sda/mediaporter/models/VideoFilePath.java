package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.metadata.Audio;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.models.metadata.Video;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video_file_paths")
public class VideoFilePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @OneToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @OneToMany(mappedBy = "videoFilePath",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<Audio> audios;

    @OneToMany(mappedBy = "videoFilePath"
            ,fetch = FetchType.EAGER
            ,cascade = CascadeType.ALL
            ,orphanRemoval = true)
    @JsonManagedReference
    private List<Subtitle> subtitles;

    @ManyToOne
    @JoinColumn(name = "source_path")
    @JsonManagedReference
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
