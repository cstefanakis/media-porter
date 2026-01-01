package org.sda.mediaporter.services.tvShowServices.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowEpisodeDto;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.metadata.Character;
import org.sda.mediaporter.services.*;
import org.sda.mediaporter.api.TheMovieDbTvShowEpisodes;
import org.sda.mediaporter.models.Contributor;
import org.sda.mediaporter.models.TvShow;
import org.sda.mediaporter.models.TvShowEpisode;
import org.sda.mediaporter.repositories.TvShowEpisodeRepository;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.tvShowServices.TvShowEpisodeService;
import org.sda.mediaporter.services.tvShowServices.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TvShowEpisodeServiceImpl implements TvShowEpisodeService {
    private final TvShowEpisodeRepository tvShowEpisodeRepository;
    private final FileService fileService;
    private final TvShowService tvShowService;
    private final ContributorService contributorService;
    private final CharacterService characterService;

    @Autowired
    public TvShowEpisodeServiceImpl(TvShowEpisodeRepository tvShowEpisodeRepository, FileService fileService, TvShowService tvShowService, ContributorService contributorService, CharacterService characterService) {
        this.tvShowEpisodeRepository = tvShowEpisodeRepository;
        this.fileService = fileService;
        this.tvShowService = tvShowService;
        this.contributorService = contributorService;
        this.characterService = characterService;
    }


    @Override
    public TvShowEpisode getTvShowEpisodeById(Long Id) {
        return null;
    }

    @Override
    public boolean deleteTvShowEpisodeById(Long id) {
        return false;
    }

    @Override
    public TvShowEpisode updateTvShowEpisodeById(Long id) {
        return null;
    }

    @Override
    public Page<TvShowEpisode> getTvShowEpisodesByTvShowIdAndSeason(Long tvShowId, Integer season, Pageable pageable) {
        return null;
    }

    @Override
    public TvShowEpisode createTvShowEpisodeFromPath(Path videoFilePath) {
        String filename = videoFilePath.getFileName().toString();
        LocalDateTime modificationDateTime = fileService.getModificationLocalDateTimeOfPath(videoFilePath);

        Integer season = getEpisodeSeasonNumberFromVideoFile(filename, seasonRegexes());
        Integer episode = getEpisodeSeasonNumberFromVideoFile(filename, episodeRegexes());
        String tvShowTitle = getTvShowTitleFromVideoFilename(filename);

        if (season == null && episode == null) {
            throw new EntityNotFoundException("TvShow not found");
        }
        TvShow tvShow = tvShowService.getOrCreateTvShowByTitle(tvShowTitle);
//        tvShow = tvShowService.updateTvShowModificationDateTime(tvShow, modificationDateTime);
        TvShowEpisode tvShowEpisode = getOrCreateTvShowEpisode(tvShow, season, episode);
        if(tvShowEpisode != null){
            updateTvShowEpisodeModificationDateTime(tvShowEpisode, modificationDateTime);
            return tvShowEpisodeRepository.save(tvShowEpisode);
        }else{
            throw new EntityNotFoundException("TvShow not found");
        }
    }

    @Override
    public void updateModificationDateTime(TvShowEpisode tvShowEpisode, Path filePath) {
        LocalDateTime modificationDateTime = fileService.getModificationLocalDateTimeOfPath(filePath);
        tvShowEpisode.setModificationDateTime(modificationDateTime);
        tvShowEpisodeRepository.save(tvShowEpisode);
    }

    @Override
    public TvShowEpisode getTvShowEpisodeByPathOrNull(String filePathWithoutTvShowSourcePath) {
        Optional<TvShowEpisode> tvShowEpisodeOptional = tvShowEpisodeRepository.findTvShowEpisodeByPath(filePathWithoutTvShowSourcePath);
        return tvShowEpisodeOptional.orElse(null);
    }

    private void updateTvShowEpisodeModificationDateTime(TvShowEpisode tvShowEpisode, LocalDateTime modificationDateTime){
        tvShowEpisode.setModificationDateTime(modificationDateTime);
        tvShowEpisodeRepository.save(tvShowEpisode);
    }

    private TvShowEpisode getTvShowEpisode(TvShow tvShow, int seasonNumber, int episodeNumber){
        TheMovieDbTvShowEpisodes tvShowEpisode = new TheMovieDbTvShowEpisodes(tvShow.getTheMoveDBTvShowId(), seasonNumber, episodeNumber);
        TheMovieDbTvShowEpisodeDto theMovieDbTvShowEpisodeDto = tvShowEpisode.getTheMovieDbTvShowEpisodeDto();
        List<Contributor> actors = contributorService.getCastsByTheMovieDbCastsDto(theMovieDbTvShowEpisodeDto.getActors());
        List<Contributor> directors = contributorService.getCrewsByTheMovieDbCrewsDto(theMovieDbTvShowEpisodeDto.getDirectors());
        List<Contributor> writers = contributorService.getCrewsByTheMovieDbCrewsDto(theMovieDbTvShowEpisodeDto.getWriters());
        return toEntity(tvShow, theMovieDbTvShowEpisodeDto,actors, directors, writers);
    }

    @Transactional
    private TvShowEpisode getOrCreateTvShowEpisode(TvShow tvShow, int seasonNumber, int episodeNumber){

        Long theMovieDbTvShowId = tvShow.getTheMoveDBTvShowId();
        TheMovieDbTvShowEpisodes theMovieDbTvShowEpisodes = new TheMovieDbTvShowEpisodes(theMovieDbTvShowId, seasonNumber, episodeNumber);
        TheMovieDbTvShowEpisodeDto theMovieDbTvShowEpisodeDto = theMovieDbTvShowEpisodes.getTheMovieDbTvShowEpisodeDto();
        if(theMovieDbTvShowEpisodeDto != null){
            Long theMovieDbTvShowEpisodeId = theMovieDbTvShowEpisodes.getTheMovieDbTvShowEpisodeDto().getTheMovieDbId();
            Optional<TvShowEpisode> tvShowEpisodeOptional = tvShowEpisodeRepository.findTvShowEpisodeByTheMovieDbId(theMovieDbTvShowEpisodeId);
            if(tvShowEpisodeOptional.isPresent()){
                return tvShowEpisodeOptional.get();
            }else {
                List<Contributor> writers = contributorService.getOrCreateCrewsByTheMovieDbCrewsDto(theMovieDbTvShowEpisodeDto.getWriters());
                List<Contributor> actors = contributorService.getOrCreateCastsByTheMovieDbCastsDto(theMovieDbTvShowEpisodeDto.getActors());
                List<Contributor> directors = contributorService.getOrCreateCrewsByTheMovieDbCrewsDto(theMovieDbTvShowEpisodeDto.getDirectors());
                TvShowEpisode tvShowEpisode = tvShowEpisodeRepository.save(toEntity(tvShow, theMovieDbTvShowEpisodeDto, actors, directors, writers));
                List<Character> characters = characterService.createCharactersForTvShowEpisode(theMovieDbTvShowEpisodeDto.getActors(), tvShowEpisode);
                tvShowEpisode.setCharacters(characters);
                return tvShowEpisodeRepository.save(tvShowEpisode);
            }
        }
        return null;
    }


    private TvShowEpisode toEntity(TvShow tvShow, TheMovieDbTvShowEpisodeDto theMovieDbTvShowEpisodeDto, List<Contributor> actors, List<Contributor> directors, List<Contributor> writers){
        return TvShowEpisode.builder()
                .tvShow(tvShow)
                .airDate(theMovieDbTvShowEpisodeDto.getAirDate())
                .actors(actors)
                .directors(directors)
                .writers(writers)
                .episodeName(theMovieDbTvShowEpisodeDto.getEpisodeName())
                .episodeNumber(theMovieDbTvShowEpisodeDto.getEpisodeNumber())
                .seasonNumber(theMovieDbTvShowEpisodeDto.getSeasonNumber())
                .type(theMovieDbTvShowEpisodeDto.getEpisodeType())
                .rating(theMovieDbTvShowEpisodeDto.getRate())
                .overview(theMovieDbTvShowEpisodeDto.getOverview())
                .poster(theMovieDbTvShowEpisodeDto.getPoster())
                .theMovieDbId(theMovieDbTvShowEpisodeDto.getTheMovieDbId())
                .build();
    }

    private String getTvShowTitleFromVideoFilename(String filename){
        Matcher macher = macher(filename, "S\\d{1,2}");
        return macher.find()
                ? filename.substring(0, macher.start()).trim().replaceAll("[._-]", " ").trim()
                : null;
    }

    private Matcher macher(String filename, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(filename);
    }

    private String[] episodeRegexes(){
        return new String[]{
                "S\\d{1,2}E(\\d{1,2})",             // S01E01
                "\\d{1,2}x(\\d{1,2})",              // 1x02
                "Episode\\s*(\\d{1,2})",            // Episode 14
                "episode(\\d{1,2})",                // episode5
                "Ep\\s*(\\d{1,2})",                 // Ep 4
                "e(\\d{1,2})"                       // e7
        };
    }

    private String[] seasonRegexes(){
        return new String[]{
                "S(\\d{1,2})E\\d{1,2}",        // S01E01 or s1e2
                "(\\d{1,2})x\\d{1,2}",         // 1x01
                "Season\\s*(\\d{1,2})",        // Season 1
                "season\\s*(\\d{1,2})",        // lowercase
                "season(\\d{1,2})"             // season1episode1 (no space)
        };
    }

    private Integer getEpisodeSeasonNumberFromVideoFile(String filename, String[] regexes){
        for (String regex : regexes) {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(filename);
            if (matcher.find()) {
                try {
                    return Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException ignored) {}
            }
        }
        return null; // No match found
    }
}
