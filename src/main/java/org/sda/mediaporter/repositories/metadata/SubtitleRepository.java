package org.sda.mediaporter.repositories.metadata;

import org.sda.mediaporter.models.metadata.Subtitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtitleRepository extends JpaRepository<Subtitle, Long> {

}
