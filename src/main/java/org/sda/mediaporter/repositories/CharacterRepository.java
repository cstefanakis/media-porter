package org.sda.mediaporter.repositories;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.sda.mediaporter.models.metadata.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Character c
            WHERE c.movie = :movie
            """)
    void deleteMovieCharactersIds(Movie movie);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Character c
            WHERE c.movie IS NULL
               AND c.contributor IS NULL
               AND c.tvShow IS NULL
               AND c.tvShowEpisode IS NULL
            """)
    void deleteCharactersWithoutPosition();
}
