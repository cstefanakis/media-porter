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

    @Query("select c from Country c where lower(trim(c.englishName)) = lower(trim(:countryName)) or lower(trim(c.nativeName)) = lower(trim(:countryName))")
    Optional<Country> findCountryByName(@Param ("countryName") String countryName);
}
