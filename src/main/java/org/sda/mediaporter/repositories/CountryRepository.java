package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("""
    SELECT c FROM Country c
    WHERE LOWER(TRIM(c.iso2Code)) = LOWER(TRIM(:code))
       OR LOWER(TRIM(c.iso3Code)) = LOWER(TRIM(:code))
    """)
    Optional<Country> findCountryByCode(@Param("code") String code);

    @Query("""
            SELECT c FROM Country c
            WHERE LOWER(TRIM(c.englishName)) = LOWER(TRIM(:countryName))
                OR LOWER(TRIM(c.nativeName)) = LOWER(TRIM(:countryName))
           """)
    Optional<Country> findCountryByName(@Param ("countryName") String countryName);

    @Query("""
            SELECT c FROM Country c
            WHERE LOWER(TRIM(c.englishName)) = LOWER(TRIM(:country))
                OR LOWER(TRIM(c.nativeName)) = LOWER(TRIM(:country))
                OR LOWER(TRIM(c.iso2Code)) = LOWER(TRIM(:country))
                OR LOWER(TRIM(c.iso3Code)) = LOWER(TRIM(:country))
           """)
    Optional<Country> findCountryByNameOrCode(@Param ("country") String country);
}
