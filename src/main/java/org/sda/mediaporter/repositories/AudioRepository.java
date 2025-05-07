package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {
}
