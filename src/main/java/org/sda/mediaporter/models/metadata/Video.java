package org.sda.mediaporter.models.metadata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.Codec;
import org.sda.mediaporter.models.Movie;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String resolution;
    private Integer bitrate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Codec codec;

    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
