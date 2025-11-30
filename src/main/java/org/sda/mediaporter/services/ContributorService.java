package org.sda.mediaporter.services;

import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCastDto;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbCrewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.models.Contributor;

import java.util.List;

public interface ContributorService {
    Contributor getContributorById(Long id);
    Contributor getContributorByFullName(String fullName);
    Page<Contributor> getAllContributors(Pageable pageable);
    Contributor getOrCreate(String fullName);
    List<Contributor> getCrewsByTheMovieDbCrewsDto(List<TheMovieDbCrewDto> theMovieDbCrewsDto);
    List<Contributor> getCastsByTheMovieDbCastsDto(List<TheMovieDbCastDto> theMovieDbCastsDto);
    List<Contributor> getOrCreateCrewsByTheMovieDbCrewsDto(List<TheMovieDbCrewDto> theMovieDbCrewsDto);
    List<Contributor> getOrCreateCastsByTheMovieDbCastsDto(List<TheMovieDbCastDto> theMovieDbCastsDto);
}
