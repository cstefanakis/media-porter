package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.metadata.Character;

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
    @JsonManagedReference
    private Gender gender;

    //TvShow
    @ManyToMany(mappedBy = "actors")
    @JsonBackReference
    private List<TvShowEpisode> tvShowEpisodeActors;

    @ManyToMany(mappedBy = "writers")
    @JsonBackReference
    private List<TvShowEpisode> tvShowEpisodeWriters;

    @ManyToMany(mappedBy = "directors")
    @JsonBackReference
    private List<TvShowEpisode> tvShowEpisodeDirectors;

    //Movie
    @ManyToMany(mappedBy = "actors")
    @JsonBackReference
    private List<Movie> movieActors;

    @ManyToMany(mappedBy = "writers")
    @JsonBackReference
    private List<Movie> movieWriters;

    @ManyToMany(mappedBy = "directors")
    @JsonBackReference
    private List<Movie> movieDirectors;

    @OneToMany(mappedBy = "contributor")
    @JsonBackReference
    private List<Character> characters;
}
