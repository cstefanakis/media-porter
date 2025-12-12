package org.sda.mediaporter.services.impl;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.models.metadata.Character;
import org.sda.mediaporter.repositories.CharacterRepository;
import org.sda.mediaporter.services.CharacterService;
import org.sda.mediaporter.services.ContributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CharacterServiceImpl  implements CharacterService {
    private final CharacterRepository characterRepository;
    private final ContributorService contributorService;

    @Autowired
    public CharacterServiceImpl(CharacterRepository characterRepository, ContributorService contributorService) {
        this.characterRepository = characterRepository;
        this.contributorService = contributorService;
    }



    private Character toEntity(String characterName, Movie movie,TvShow tvShow, TvShowEpisode tvShowEpisode, Contributor contributor){
        return Character.builder()
                .name(characterName)
                .movie(movie)
                .tvShow(tvShow)
                .tvShowEpisode(tvShowEpisode)
                .contributor(contributor)
                .build();
    }

    @Override
    public List<Character> createCharactersForMovie(List<TheMovieDbCastDto> actors, Movie movie) {
        return actors.stream()
                .map(a -> createCharacter(a.getTheMovieDbId(), a.getCharacter(), movie, null, null)).toList();
    }

    @Override
    public List<Character> createCharactersForTvShow(List<TheMovieDbCastDto> actors, TvShow tvShow) {
        return actors.stream()
                .map(a -> createCharacter(a.getTheMovieDbId(), a.getCharacter(), null, tvShow, null)).toList();
    }

    @Override
    public List<Character> createCharactersForTvShowEpisode(List<TheMovieDbCastDto> actors, TvShowEpisode tvShowEpisode) {
        return actors.stream()
                .map(a -> createCharacter(a.getTheMovieDbId(), a.getCharacter(), null, null, tvShowEpisode)).toList();
    }

    private Character createCharacter(Long theMovieDbId, String characterName, Movie movie, TvShow tvShow, TvShowEpisode tvShowEpisode) {
        Contributor contributor = contributorService.getContributorByTheMovieDbIdOrNull(theMovieDbId);
        if(contributor != null && characterName != null && !characterName.isBlank() ){
            return characterRepository.save(toEntity(characterName, movie, tvShow, tvShowEpisode, contributor));
        }else{
            return null;
        }
    }

}
