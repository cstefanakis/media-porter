package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.Movie;

@Entity
@Table(name = "subtitles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Subtitle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_ids")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "format_ids")
    private Codec format;

    @ManyToOne
    @JoinColumn (name = "movie_ids")
    @JsonBackReference
    private Movie movie;
}
