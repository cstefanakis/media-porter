package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "contributors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contributor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_names")
    private String fullName;

    @Column(name = "posters")
    private String poster;

    @Column(name = "web_sites")
    private String website;

    @ManyToMany(mappedBy = "actors")
    @JsonBackReference("tvShow_actors")
    private List<TvShowEpisode> tvShowEpisodeActors;

    @ManyToMany(mappedBy = "writers")
    @JsonBackReference("tvShow_writers")
    private List<TvShowEpisode> tvShowEpisodeWriters;

    @ManyToMany(mappedBy = "directors")
    @JsonBackReference("tvShow_directors")
    private List<TvShowEpisode> tvShowEpisodeDirectors;
}
