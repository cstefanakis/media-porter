package org.sda.mediaporter.repositories.metadata;

import org.sda.mediaporter.models.metadata.AudioChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioChannelRepository extends JpaRepository<AudioChannel, Long> {
    @Query("""
            SELECT ac FROM AudioChannel ac
            WHERE ac.channels = :channels
            """)
    Optional<AudioChannel> findAudioChannelByChannel(@Param("channels") Integer channels);

    @Query("""
            SELECT ac FROM AudioChannel ac
            WHERE LOWER(TRIM(ac.title)) = LOWER(TRIM(:title))
            """)
    Optional<AudioChannel> findAudioChannelByTitle(@Param("title") String title);
}
