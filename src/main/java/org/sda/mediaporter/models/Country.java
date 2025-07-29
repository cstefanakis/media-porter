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

    @NotEmpty(message = "ISO 2 code must not be empty")
    @Column(name = "iso_2_codes", nullable = false, length = 2, unique = true)
    private String iso2Code;

    @NotEmpty(message = "ISO 3 code must not be empty")
    @Column(name = "iso_3_codes", nullable = false, length = 3, unique = true)
    private String iso3Code;

    @NotEmpty(message = "English name must not be empty")
    @Column(name = "english_names", nullable = false)
    private String englishName;

    @NotEmpty(message = "Native name must not be empty")
    @Column(name = "native_names", nullable = false)
    private String nativeName;

}
