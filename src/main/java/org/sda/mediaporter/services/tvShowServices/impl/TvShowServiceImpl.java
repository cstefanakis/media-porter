package org.sda.mediaporter.services.tvShowServices.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.fileServices.VideoFilePathService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sda.mediaporter.api.TheMovieDbCreditsForTvShowById;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowDto;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.metadata.Character;
import org.sda.mediaporter.services.*;
import org.sda.mediaporter.api.TheMovieDbTvShowSearch;
import org.sda.mediaporter.api.TheMovieDbTvShowsById;
import org.sda.mediaporter.dtos.theMovieDbDtos.TheMovieDbTvShowSearchDTO;
import org.sda.mediaporter.repositories.*;
import org.sda.mediaporter.services.fileServices.FileService;
import org.sda.mediaporter.services.tvShowServices.TvShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TvShowServiceImpl implements TvShowService {
    private final GenreService genreService;
    private final CountryService countryService;
    private final TvShowRepository tvShowRepository;
    private final FileService fileService;
    private final ContributorService contributorService;
    private final LanguageService languageService;
    private final CharacterService characterService;
    private final VideoFilePathService videoFilePathService;

    @Autowired
    public TvShowServiceImpl(GenreService genreService, CountryService countryService, TvShowRepository tvShowRepository, FileService fileService, ContributorService contributorService, LanguageService languageService, CharacterService characterService, VideoFilePathService videoFilePathService) {
        this.genreService = genreService;
        this.countryService = countryService;
        this.tvShowRepository = tvShowRepository;
        this.fileService = fileService;
        this.contributorService = contributorService;
        this.languageService = languageService;
        this.characterService = characterService;
        this.videoFilePathService = videoFilePathService;
    }

    @Override
    public TvShow updateTvShowModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime) {
        tvShow.setLastModificationDateTime(modificationDateTime);
        return tvShowRepository.save(tvShow);
    }

    @Override
    @Transactional
    public TvShow getOrCreateTvShowByTitle(String fileTitle) {
        String filteredFileTitle = fileTitle.replace(fileService.getFileExtensionWithDot(fileTitle), "");
        filteredFileTitle = fileService.getSafeFileName(filteredFileTitle);
        TheMovieDbTvShowSearchDTO tvShowFromSearchDTO = getTvShowAPISearchDTO(filteredFileTitle);
        if(tvShowFromSearchDTO != null){
            Long theMovieDbTvShowId = tvShowFromSearchDTO.getTheMovieDbId();
            Optional<TvShow> tvShowOptional = tvShowRepository.findTvShowByTheMovieDBId(theMovieDbTvShowId);
            if(tvShowOptional.isPresent()){
                return tvShowOptional.get();
            }else{
                TheMovieDbTvShowsById theMovieDbTvShowsById = new TheMovieDbTvShowsById(theMovieDbTvShowId);
                TheMovieDbCreditsForTvShowById theMovieDbCreditsForTvShowById = new TheMovieDbCreditsForTvShowById(theMovieDbTvShowId);
                List<Contributor> writers = contributorService.getOrCreateCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForTvShowById.getWriters());
                List<Contributor> actors = contributorService.getOrCreateCastsByTheMovieDbCastsDto(theMovieDbCreditsForTvShowById.getActors());
                List<Contributor> directors = contributorService.getOrCreateCrewsByTheMovieDbCrewsDto(theMovieDbCreditsForTvShowById.getDirectors());
                List<Genre> genres = genreService.getOrCreateGenresByTitles(theMovieDbTvShowsById.getTheMovieDbTvShowDto().getGenres());
                Language originalLanguage = languageService.getLanguageByCodeOrNull(theMovieDbTvShowsById.getTheMovieDbTvShowDto().getLanguageCode());
                List<Country> countries = countryService.getCountriesByCodes(theMovieDbTvShowsById.getTheMovieDbTvShowDto().getCountriesCodes());
                TvShow tvShow = toEntity(theMovieDbTvShowsById.getTheMovieDbTvShowDto(), writers, actors, directors, genres, originalLanguage, countries, theMovieDbTvShowId);
                tvShowRepository.save(tvShow);
                List<Character> characters = characterService.createCharactersForTvShow(theMovieDbCreditsForTvShowById.getActors(), tvShow);
                tvShow.setCharacters(characters);
                return tvShow;
            }
        }
        return null;
    }

    @Override
    public void updateModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime) {
        tvShow.setLastModificationDateTime(modificationDateTime);
        tvShowRepository.save(tvShow);
    }

    @Override
    public TvShow getTvShowById(Long id) {
        Optional<TvShow> tvShowOptional = tvShowRepository.findById(id);
        return tvShowOptional.orElseThrow(() -> new EntityNotFoundException(String.format("Tv Show with id %s not fount",id)));
    }

    @Override
    public Page<TvShow> getTvShows(Pageable pageable) {
        return tvShowRepository.findAll(pageable);
    }

    @Override
    public List<TvShowEpisode> getTvShowEpisodesOlderThanXDays(int days) {
        return List.of();
    }

    @Override
    public List<Long> getTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(Integer days, SourcePath sourcePath) {
        LocalDateTime localDateTimeBeforeDays = LocalDateTime.now().minusDays(days);
        return tvShowRepository.findTvShowsVideoFilePathsThatTvShowIsOlderThanXDaysAndBySourcePath(localDateTimeBeforeDays, sourcePath);
    }

    @Override
    public Long getTvShowIdByVideoFilePathId(Long videoFilePathId) {
        return tvShowRepository.findTvShowIdByVideoFilePathId(videoFilePathId);
    }

    @Override
    public void deleteTvShowWithoutTvShowEpisodes(Long tvShowId) {
        tvShowRepository.deleteTvShowWithoutTvShowEpisodes(tvShowId);
    }

    @Override
    public void deleteVideoFilePathsFromTvShowsWithUnveiledPath() {
        List<Long> tvShowVideoFilePathsIss = videoFilePathService.getTvShowsVideoFilePathIdsByLibraryItems(LibraryItems.TV_SHOW);
        for(Long videoFilePathId : tvShowVideoFilePathsIss){
            Path fullFilePath = videoFilePathService.getFullPathFromVideoFilePathId(videoFilePathId);
            if(!fileService.isFilePathExist(fullFilePath)){
                videoFilePathService.deleteVideoFilePathById(videoFilePathId);
            }
        }
    }

    @Override
    public void deleteTvShowsWithoutTvShowEpisodes() {
        tvShowRepository.deleteTvShowsWithoutTvShowEpisodes();
    }

    private TvShow toEntity(TheMovieDbTvShowDto theMovieDbTvShowDto,List<Contributor> writers, List<Contributor> actors, List<Contributor> directors, List<Genre> genres, Language originalLanguage, List<Country> countries, Long theMovieDbTvShowId){
        return TvShow.builder()
                .title(theMovieDbTvShowDto.getTitle())
                .originalTitle(theMovieDbTvShowDto.getOriginalTitle())
                .year(theMovieDbTvShowDto.getYear())
                .firstAirDate(theMovieDbTvShowDto.getFirstAirDate())
                .lastAirDate(theMovieDbTvShowDto.getLastAirDate())
                .overview(theMovieDbTvShowDto.getOverview())
                .homePage(theMovieDbTvShowDto.getHomePage())
                .poster(theMovieDbTvShowDto.getPoster())
                .rate(theMovieDbTvShowDto.getRate())
                .status(theMovieDbTvShowDto.getStatus())
                .theMoveDBTvShowId(theMovieDbTvShowId)
                .countries(countries)
                .genres(genres)
                .writers(writers)
                .actors(actors)
                .directors(directors)
                .originalLanguage(originalLanguage)
                .build();
    }

    @Override
    public TheMovieDbTvShowSearchDTO getTvShowAPISearchDTO(String searchTitle) {
        String[] searchTitleWords = searchTitle.split(" ");
        int fileTitleWordsLength = searchTitleWords.length;
        for (int i = 0; i < fileTitleWordsLength; i++) {
            String[] fileTitleWordsWithoutLastWord = Arrays.copyOf(searchTitleWords, fileTitleWordsLength - i);
            String newSearchTitle = String.join(" ", fileTitleWordsWithoutLastWord).replaceAll(" ", "+");

            TheMovieDbTvShowSearch theMovieDbTvShowSearch = new TheMovieDbTvShowSearch(newSearchTitle);
            List<TheMovieDbTvShowSearchDTO> tvShowAPISearchDTOs = theMovieDbTvShowSearch.getTvShows();

            int tvShowSearchSize = tvShowAPISearchDTOs.size();

            if (tvShowSearchSize == 1) {
                return tvShowAPISearchDTOs.getFirst();
            }

            if (tvShowSearchSize > 1) {
                TheMovieDbTvShowSearchDTO filteredTvShowDto = filteredTvShowDto(newSearchTitle, tvShowAPISearchDTOs);
                if (filteredTvShowDto != null) {
                    return filteredTvShowDto;
                }
            }
        }
        return null;
    }

    private TheMovieDbTvShowSearchDTO filteredTvShowDto(String searchTitle, List<TheMovieDbTvShowSearchDTO> listOfTvShows){
        for(TheMovieDbTvShowSearchDTO theMovieDbTvShowDto : listOfTvShows){
            String searchTitleWithoutSymbols = fileService.getSafeFileName(searchTitle).toLowerCase();
            String originalTitleWithoutSymbols = fileService.getStringWithoutDiacritics(theMovieDbTvShowDto.getOriginalTitle());
            originalTitleWithoutSymbols = fileService.getSafeFileName(originalTitleWithoutSymbols).toLowerCase();
            String titleWithoutSymbols = fileService.getStringWithoutDiacritics(theMovieDbTvShowDto.getTitle());
            titleWithoutSymbols = fileService.getSafeFileName(titleWithoutSymbols).toLowerCase();
            if(isTheSameSearchTitleWithOriginalTitleOrTitle(searchTitleWithoutSymbols, originalTitleWithoutSymbols, titleWithoutSymbols)){
                return theMovieDbTvShowDto;
            }
            if(isTheSearchTitleLengthSameWithLengthOfTitle(searchTitleWithoutSymbols, originalTitleWithoutSymbols, titleWithoutSymbols)){
                return theMovieDbTvShowDto;
            }
        }
        return null;
    }

    private boolean isTheSameSearchTitleWithOriginalTitleOrTitle(String searchTitle, String originalTitle, String title){
        return searchTitle.equals(originalTitle) || searchTitle.equals(title);
    }

    private boolean isTheSearchTitleLengthSameWithLengthOfTitle(String searchTitle, String originalTitle, String title){
        int searchTitleWords = searchTitle.split(" ").length;
        int originalTitleWords = originalTitle.split(" ").length;
        int titleWords = title.split(" ").length;
        return searchTitleWords == originalTitleWords || searchTitleWords == titleWords;
    }
}
