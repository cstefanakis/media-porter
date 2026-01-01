package org.sda.mediaporter.models.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.models.TvShowEpisode;


@Entity
@Table(name = "characters")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "contributor_id")
    @JsonManagedReference
    private Contributor contributor;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "tvShow_id")
    @JsonBackReference
    private TvShow tvShow;

    @ManyToOne
    @JoinColumn(name = "tvShowEpisode_id")
    @JsonBackReference
    private TvShowEpisode tvShowEpisode;
}
