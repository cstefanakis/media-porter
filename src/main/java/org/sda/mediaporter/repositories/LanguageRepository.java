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
        select l from Language l 
        where lower(trim(l.englishTitle)) = lower(trim(:languageTitle)) 
            or lower(trim(l.originalTitle)) = lower(trim(:languageTitle))
    """)
    Optional<Language> findByTitle(@Param("languageTitle") String languageTitle);

    @Query("""
        select l from Language l 
        where lower(trim(l.iso6391)) = lower(trim(:code)) 
            or lower(trim(l.iso6392B)) = lower(trim(:code)) 
            or lower(trim(l.iso6392T)) = lower(trim(:code))
    """)
    Optional<Language> findByCode(@Param("code") String code);
}
