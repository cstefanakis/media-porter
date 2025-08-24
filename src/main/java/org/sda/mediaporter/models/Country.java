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

    @Column(name = "iso_2_codes", length = 2, unique = true)
    private String iso2Code;

    @Column(name = "iso_3_codes",length = 3, unique = true)
    private String iso3Code;

    @Column(name = "english_names", nullable = false)
    private String englishName;

    @Column(name = "native_names")
    private String nativeName;

}
