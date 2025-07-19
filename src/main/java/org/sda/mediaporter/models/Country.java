package org.sda.mediaporter.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotEmpty
    @Column(name = "iso2")
    private String iso2Code;
    @NotEmpty
    @Column(name = "iso3")
    private String iso3Code;
    @NotEmpty
    private String englishName;
    @NotEmpty
    private String nativeName;
}
