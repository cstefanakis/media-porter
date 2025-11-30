package org.sda.mediaporter.repositories.metadata;

import org.sda.mediaporter.models.VideoFilePath;
import org.sda.mediaporter.models.metadata.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
