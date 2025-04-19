package org.sda.mediaporter.models.multimedia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sda.mediaporter.models.multimedia.videoFiles.Movie;
import org.sda.mediaporter.models.multimedia.videoFiles.TvShow;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String code;
    @ManyToMany(mappedBy = "audioLanguages")
    private List<Movie> moviesAudioLanguage;
    @ManyToMany(mappedBy = "subtitleLanguages")
    private List<Movie> moviesSubtitleLanguage;
    @ManyToMany(mappedBy = "audioLanguages")
    private List<TvShow> tvShowsAudioLanguage;
    @ManyToMany(mappedBy = "subtitleLanguages")
    private List<TvShow> tvShowsSubtitleLanguage;
}
