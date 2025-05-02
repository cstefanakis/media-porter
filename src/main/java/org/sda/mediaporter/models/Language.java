package org.sda.mediaporter.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String englishTitle;
    private String originalTitle;
    private String iso2;
    private String iso3;

    @ManyToMany(mappedBy = "languages", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Movie> movies = new ArrayList<>();
}
