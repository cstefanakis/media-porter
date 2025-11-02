package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tv_shows_episodes")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TvShowEpisode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tv_shows_ids")
    @JsonBackReference("tv_shows_ids")
    private TvShow tvShow;

    @Column(name = "air_dates")
    private LocalDate airDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tv_show_actors",
            joinColumns = @JoinColumn(name = "tv_show_episodes_ids"),
            inverseJoinColumns = @JoinColumn(name = "actors_ids")
    )
    @JsonManagedReference("tv_show_actors")
    private List<Contributor> actors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tv_show_writers",
            joinColumns = @JoinColumn(name = "tv_show_episodes_ids"),
            inverseJoinColumns = @JoinColumn(name = "writers_ids")
    )
    @JsonManagedReference("tv_show_writers")
    private List<Contributor> writers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tv_show_directors",
            joinColumns = @JoinColumn(name = "tv_show_episodes_ids"),
            inverseJoinColumns = @JoinColumn(name = "directors_ids")
    )
    @JsonManagedReference("tv_show_directors")
    private List<Contributor> directors;

    @Column(name = "episode_number")
    private Integer episodeNumber;

    @Column (name = "season_number")
    private Integer seasonNumber;

    @Column (name = "type")
    private String type;

    @Column (name = "episode_mame")
    private String episodeName;

    @Column (name = "rating")
    private Double rating;

    @Column (name = "overview",
            columnDefinition = "TEXT")
    private String overview;

    @Column (name = "images_url")
    private String imageUrl;

}
