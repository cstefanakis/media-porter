package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.metadata.Codec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodecRepository extends JpaRepository<Codec, Long> {
    @Query("select c from Codec c where c.name = :name")
    Optional <Codec> findByName(@Param("name") String name);
}
