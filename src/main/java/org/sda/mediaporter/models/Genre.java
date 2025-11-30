package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    // Many-to-Many mapping to genres using the tv_show_genres join table
    @ManyToMany(mappedBy = "genres")
    private List<TvShow> tvShowsGenres;

    // Many-to-Many mapping to genres using the movie_genres join table
    @ManyToMany(mappedBy = "genres")
    private List<Movie> movieGenres;
}
