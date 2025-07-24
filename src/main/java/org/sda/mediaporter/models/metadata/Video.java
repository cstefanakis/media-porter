package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Movie;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resolution_ids")
    private Resolution resolution;

    @Column(name = "bitrates")
    private Integer bitrate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Codec codec;

    @OneToOne
    @JoinColumn(name = "movie_ids")
    @JsonBackReference
    private Movie movie;
}
