package org.sda.mediaporter.repositories.metadata;

import org.sda.mediaporter.models.metadata.AudioChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioChannelRepository extends JpaRepository<AudioChannel, Long> {
    @Query("select ac from AudioChannel ac where ac.channels = :channels")
    Optional<AudioChannel> findAudioChannelsByChannel(@Param("channels") Integer channels);
}
