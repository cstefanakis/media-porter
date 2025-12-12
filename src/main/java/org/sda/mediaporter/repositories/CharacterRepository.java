package org.sda.mediaporter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sda.mediaporter.models.metadata.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
}
