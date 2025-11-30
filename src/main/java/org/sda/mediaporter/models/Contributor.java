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

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "poster")
    private String poster;

    @Column(name = "theMovieDb_id")
    private Long theMovieDbId;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    //TvShow
    @ManyToMany(mappedBy = "actors")
    private List<TvShowEpisode> tvShowEpisodeActors;

    @ManyToMany(mappedBy = "writers")
    private List<TvShowEpisode> tvShowEpisodeWriters;

    @ManyToMany(mappedBy = "directors")
    private List<TvShowEpisode> tvShowEpisodeDirectors;

    //Movie
    @ManyToMany(mappedBy = "actors")
    private List<Movie> movieActors;

    @ManyToMany(mappedBy = "writers")
    private List<Movie> movieWriters;

    @ManyToMany(mappedBy = "directors")
    private List<Movie> movieDirectors;
}
