package org.sda.mediaporter.repositories.metadata;

import org.sda.mediaporter.models.metadata.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface
VideoRepository extends JpaRepository<Video, Long> {
}
