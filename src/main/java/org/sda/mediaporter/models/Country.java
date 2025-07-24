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
    @Column(name = "iso_2_codes")
    private String iso2Code;

    @NotEmpty
    @Column(name = "iso_3_codes")
    private String iso3Code;

    @NotEmpty
    @Column(name = "english_names")
    private String englishName;

    @NotEmpty
    @Column(name = "native_names")
    private String nativeName;
}
