package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.models.VideoFilePath;

import java.util.List;

@Entity
@Table(name = "audios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bitrate")
    private Integer bitrate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "audio_channel_id")
    private AudioChannel audioChannel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codec_id")
    private Codec codec;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id")
    @JsonManagedReference("audioLanguages")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_file_path_id")
    VideoFilePath videoFilePath;

}
