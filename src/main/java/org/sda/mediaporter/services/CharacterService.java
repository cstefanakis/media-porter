package org.sda.mediaporter.services;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.metadata.Character;

import java.util.List;

public interface CharacterService {

    List<Character> createCharactersForMovie(List<TheMovieDbCastDto> actors, Movie movie);
}
