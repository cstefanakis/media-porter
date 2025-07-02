package org.sda.mediaporter.repositories.metadata;

import org.apache.tomcat.util.http.parser.MediaType;
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
    @Query("select c from Codec c where c.name = :name")
    Optional <Codec> findByName(@Param("name") String name);

    @Query("select c from Codec c where lower(trim(c.name)) = lower(trim(:name)) and c.mediaType = :mediaType")
    Optional <Codec> findByNameAndMediaType(@Param("name") String name,
                                       @Param("mediaType") MediaTypes mediaType);

    @Query("select c from Codec c where c.mediaType = :mediaType")
    List<Codec> findByMediaType(@Param("mediaType") MediaTypes mediaType);

}
