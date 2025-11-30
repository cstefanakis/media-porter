package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.VideoFilePath;

@Entity
@Table(name = "videos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bitrate")
    private Integer bitrate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resolution_id")
    private Resolution resolution;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codec_id")
    private Codec codec;

    @OneToOne(mappedBy = "video")
    private VideoFilePath videoFilePath;
}
