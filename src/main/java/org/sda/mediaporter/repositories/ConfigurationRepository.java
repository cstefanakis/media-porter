package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    @Query("select c.genres from Configuration c where c = :configuration")
    List<Genre> findGenresFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("select c.videoCodecs from Configuration c where c = :configuration")
    List<Codec> findVideoCodecsFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("select c.audioCodecs from Configuration c where c = :configuration")
    List<Codec> findAudioCodecsFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("select c.audioChannels from Configuration c where c = :configuration")
    List<AudioChannel> findAudioChannelsFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("select c.audioLanguages from Configuration c where c = :configuration")
    List<Language> findLanguagesFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("select c.videoResolutions from Configuration c where c = :configuration")
    List<Resolution> findVideoResolutionsFromConfiguration(@Param("configuration") Configuration configuration);

}
