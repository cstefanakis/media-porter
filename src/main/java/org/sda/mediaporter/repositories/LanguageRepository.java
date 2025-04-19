package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.multimedia.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("select l from Language l where l.name = :name")
    Optional<Language> findByName(@Param ("name") String name);

    @Query("select l from Language l where l.code = :languageCode")
    Optional <Language> findByCode(@Param ("code") String languageCode);
}
