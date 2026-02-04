package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    @Query("""
            SELECT c.genres FROM Configuration c
                WHERE c = :configuration
            """)
    List<Genre> findGenresFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("""
            SELECT c.videoCodecs FROM Configuration c
                WHERE c = :configuration
            """)
    List<Codec> findVideoCodecsFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("""
        SELECT c.audioCodecs FROM Configuration c
            WHERE c = :configuration
        """)
    List<Codec> findAudioCodecsFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("""
        SELECT c.audioChannels FROM Configuration c
            WHERE c = :configuration
        """)
    List<AudioChannel> findAudioChannelsFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("""
        SELECT c.audioLanguages FROM Configuration c
            WHERE c = :configuration
        """)
    List<Language> findLanguagesFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("""
        SELECT c.videoResolutions FROM Configuration c
            WHERE c = :configuration
        """)
    List<Resolution> findVideoResolutionsFromConfiguration(@Param("configuration") Configuration configuration);

    @Query("""
        SELECT COUNT(c) > 0
        FROM Configuration c
        LEFT JOIN c.videoResolutions vrs
        WHERE c.sourcePath = :sourcePath
          AND (
               vrs.name = :videoResolution
               OR c.videoResolutions IS EMPTY
          )
        """)
    boolean isFileSupportSourceResolution(@Param ("videoResolution") String videoResolution,
                                          @Param("sourcePath") SourcePath sourcePath);

    @Query("""
        SELECT COUNT(c) > 0
        FROM Configuration c
        LEFT JOIN c.audioCodecs aco
        WHERE c.sourcePath = :sourcePath
          AND (
               aco.name = :audioCodec
               OR c.audioCodecs IS EMPTY
          )
        """)
    boolean isFileAudioCodecSupport(@Param("audioCodec") String audioCodec,
                                    @Param("sourcePath") SourcePath sourcePath);

    @Query("""
        SELECT COUNT(c) > 0
        FROM Configuration c
        LEFT JOIN c.videoCodecs vco
        WHERE c.sourcePath = :sourcePath
          AND (
               vco.name = :videoCodec
               OR c.videoCodecs IS EMPTY
          )
        """)
    boolean isFileSupportVideoCodec(@Param("videoCodec") String videoCodec,
                                    @Param("sourcePath") SourcePath sourcePath);

    @Query("""
        SELECT COUNT(c) > 0
        FROM Configuration c
        WHERE c.sourcePath = :sourcePath
          AND (
               :videoBitrate = NULL
               OR :videoBitrate BETWEEN c.firstAudioBitrateValueRange
               AND c.secondAudioBitrateValueRange
               OR c.videoCodecs IS EMPTY
          )
        """)
    boolean isFileVideoBitrateInRange(@Param("videoBitrate") Integer videoBitrate,
                                      @Param("sourcePath") SourcePath sourcePath);

    @Query("""
        SELECT COUNT(c) > 0
        FROM Configuration c
        LEFT JOIN c.audioChannels ach
        WHERE c.sourcePath = :sourcePath
          AND (
               ach.channels = :audioChannel
               OR c.videoCodecs IS EMPTY
          )
        """)
    boolean isFileAudioChannelsSupport(@Param("audioChannel") Integer audioChannel,
                                       @Param("sourcePath") SourcePath sourcePath);

    @Query("""
        SELECT COUNT(c) > 0
        FROM Configuration c
        LEFT JOIN c.audioLanguages ala
        WHERE c.sourcePath = :sourcePath
          AND (
               ala.iso6391 = :audioLanguage
               OR c.audioLanguages IS EMPTY
          )
        """)
    boolean isFileAudioLanguageSupport(@Param("audioLanguage") String audioLanguage,
                                       @Param("sourcePath") SourcePath sourcePath);

    @Query("""
            SELECT COUNT(c) > 0
            FROM Configuration c
            WHERE c.sourcePath = :sourcePath
            AND (
                :audioBitrate BETWEEN c.firstAudioBitrateValueRange
                    AND c.secondAudioBitrateValueRange
                OR (
                    c.firstAudioBitrateValueRange IS NULL
                    AND c.secondAudioBitrateValueRange IS NULL
                )
                OR (
                    :audioBitrate >= c.firstAudioBitrateValueRange
                    AND c.secondAudioBitrateValueRange IS NULL
                )
                OR (
                    :audioBitrate <= c.secondAudioBitrateValueRange
                    AND c.firstAudioBitrateValueRange IS NULL
                )
            )
            """)
    boolean isFileAudioBitrateInRange(@Param("audioBitrate") Integer audioBitrate,
                                      @Param("sourcePath") SourcePath sourcePath);

    @Query("""
        SELECT COUNT(c) > 0
        FROM Configuration c
        LEFT JOIN c.genres gen
        WHERE c.sourcePath = :sourcePath
          AND (
               gen = :genre
               OR c.genres IS EMPTY
          )
        """)
    boolean isFileSupportGenres(@Param("genre") Genre genre,
                                @Param("sourcePath")SourcePath sourcePath);

    @Query("""
            SELECT c FROM Configuration c
            LEFT JOIN c.sourcePath sp
            WHERE sp.id = :sourcePathId
            """)
    Optional<Configuration> findConfigurationBySourcePathId(@Param("sourcePathId") Long sourcePathId);
}
