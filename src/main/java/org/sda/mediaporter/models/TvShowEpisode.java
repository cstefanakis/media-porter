package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.sda.mediaporter.models.metadata.Character;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tv_shows_episodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TvShowEpisode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "the_movie_db_id")
    private Long theMovieDbId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_shows_ids")
    @JsonBackReference
    private TvShow tvShow;

    @Column(name = "air_dates")
    private LocalDate airDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "actors",
            joinColumns = @JoinColumn(name = "tv_show_episode_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    @JsonManagedReference
    private List<Contributor> actors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "writers",
            joinColumns = @JoinColumn(name = "tv_show_episode_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    @JsonManagedReference
    private List<Contributor> writers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "directors",
            joinColumns = @JoinColumn(name = "tv_show_episode_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    @JsonManagedReference
    private List<Contributor> directors;

    @Column(name = "episode_numbers")
    private Integer episodeNumber;

    @Column (name = "season_numbers")
    private Integer seasonNumber;

    @Column (name = "types")
    private String type;

    @Column (name = "episode_names")
    private String episodeName;

    @Column (name = "ratings")
    private Double rating;

    @Column (name = "overviews",
            columnDefinition = "TEXT")
    private String overview;

    @Column (name = "posters")
    private String poster;

    @Column (name = "modification_dates")
    private LocalDateTime modificationDateTime;

    @OneToMany(mappedBy = "tvShowEpisode",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<VideoFilePath> videoFilePaths;

    @OneToMany(mappedBy = "tvShowEpisode",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<Character> characters = new ArrayList<>();
}
