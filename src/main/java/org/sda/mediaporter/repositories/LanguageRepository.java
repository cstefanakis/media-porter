package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    @Query("select l from Language l where l.englishTitle = :title")
    Optional<Language> findByTitle(@Param("title") String title);

    @Query("select l from Language l where lower(trim(l.iso2)) = :code or lower(trim(l.iso3)) = :code ")
    Optional<Language> findByCode(@Param("code") String code);
}
