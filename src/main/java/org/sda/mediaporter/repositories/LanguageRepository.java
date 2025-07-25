package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    @Query("""
    SELECT l FROM Language l 
        WHERE LOWER(TRIM(l.englishTitle)) = LOWER(TRIM(:languageTitle)) 
        OR LOWER(TRIM(l.originalTitle)) = LOWER(TRIM(:languageTitle))
    """)
    Optional<Language> findByTitle(@Param("languageTitle") String languageTitle);

    @Query("""
    SELECT l FROM Language l
        WHERE LOWER(TRIM(l.iso6391)) = LOWER(TRIM(:code))
        OR LOWER(TRIM(l.iso6392B)) = LOWER(TRIM(:code))
        OR LOWER(TRIM(l.iso6392T)) = LOWER(TRIM(:code))
    """)
    Optional<Language> findByCode(@Param("code") String code);
}
