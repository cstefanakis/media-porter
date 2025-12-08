package org.sda.mediaporter.services.impl;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Character;
import org.sda.mediaporter.repositories.CharacterRepository;
import org.sda.mediaporter.services.CharacterService;
import org.sda.mediaporter.services.ContributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CharacterServiceImpl  implements CharacterService {
    private final CharacterRepository characterRepository;
    private final ContributorService contributorService;

    @Autowired
    public CharacterServiceImpl(CharacterRepository characterRepository, ContributorService contributorService) {
        this.characterRepository = characterRepository;
        this.contributorService = contributorService;
    }



    private Character toEntity(String characterName, Movie movie, Contributor contributor){
        return Character.builder()
                .name(characterName)
                .movie(movie)
                .contributor(contributor)
                .build();
    }

    @Override
    public List<Character> createCharactersForMovie(List<TheMovieDbCastDto> actors, Movie movie) {
        return actors.stream()
                .map(a -> createCharacter(a.getTheMovieDbId(), a.getCharacter(), movie)).toList();
    }

    private Character createCharacter(Long theMovieDbId, String characterName, Movie movie) {
        Contributor contributor = contributorService.getContributorByTheMovieDbIdOrNull(theMovieDbId);
        if(contributor != null && characterName != null && !characterName.isBlank() ){
            return characterRepository.save(toEntity(characterName, movie, contributor));
        }else{
            return null;
        }
    }
}
