package org.sda.mediaporter.models.metadata;

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
    private Contributor contributor;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "tvShow_id")
    private TvShow tvShow;
    @ManyToOne
    @JoinColumn(name = "tvShowEpisode_id")
    private TvShowEpisode tvShowEpisode;
}
