package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Movie;

@Entity
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
    private String resolution;
    private Integer bitrate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Codec codec;

    @OneToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference
    private Movie movie;
}
