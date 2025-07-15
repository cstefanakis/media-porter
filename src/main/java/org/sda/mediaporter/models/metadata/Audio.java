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
    private Integer bitrate;
    @ManyToOne(fetch = FetchType.EAGER)
    private AudioChannel audioChannel;
    @ManyToOne(fetch = FetchType.LAZY)
    private Codec codec;
    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference
    private Movie movie;
}
