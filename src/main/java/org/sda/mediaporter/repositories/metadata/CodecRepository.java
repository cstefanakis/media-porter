package org.sda.mediaporter.repositories.metadata;

import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodecRepository extends JpaRepository<Codec, Long> {
    @Query("""
            SELECT c FROM Codec c
            WHERE LOWER(TRIM(c.name)) = LOWER(TRIM(:name))
           """)
    Optional <Codec> findByName(@Param("name") String name);

    @Query("""
       SELECT c FROM Codec c
       WHERE LOWER(TRIM(c.name)) = LOWER(TRIM(:name))
         AND c.mediaType = :mediaType
       """)
    Optional<Codec> findByNameAndMediaType(@Param("name") String name,
                                           @Param("mediaType") MediaTypes mediaType);
    @Query("""
            SELECT c FROM Codec c
            WHERE c.mediaType = :mediaType
           """)
    List<Codec> findByMediaType(@Param("mediaType") MediaTypes mediaType);

    @Query("""
            SELECT c.id FROM Codec c
            WHERE c.mediaType = :mediaType
            """)
    List<Long> findIdsByMediaType(@Param("mediaType") MediaTypes mediaType);

}
