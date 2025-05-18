package org.sda.mediaporter.repositories.metadata;

import org.sda.mediaporter.models.metadata.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {
}
