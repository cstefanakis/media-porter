package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;

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

    @Column(name = "bitrates")
    private Integer bitrate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "audio_channel_ids")
    private AudioChannel audioChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codec_ids")
    private Codec codec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_ids")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "movie_ids")
    @JsonBackReference
    private Movie movie;
}
