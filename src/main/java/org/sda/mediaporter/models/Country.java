package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iso_2_code",
            length = 2,
            unique = true)
    private String iso2Code;

    @Column(name = "iso_3_code",
            length = 3,
            unique = true)
    private String iso3Code;

    @Column(name = "english_name", nullable = false)
    private String englishName;

    @Column(name = "native_name")
    private String nativeName;

    @ManyToMany(mappedBy = "countries")
    @JsonBackReference("tv_show_countries")
    private List<TvShow> tvShows;

    @ManyToMany(mappedBy = "countries")
    private List<Movie> movies;

}
