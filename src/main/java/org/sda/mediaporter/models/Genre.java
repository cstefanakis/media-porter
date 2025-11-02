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

    @ManyToMany(mappedBy = "genres")
    @JsonBackReference("genres")
    private List<Configuration> genresConfiguration;

    @ManyToMany(mappedBy = "genres")
    @JsonBackReference("tv_show_genres")
    private List<TvShow> tvShowsGenres;

}
