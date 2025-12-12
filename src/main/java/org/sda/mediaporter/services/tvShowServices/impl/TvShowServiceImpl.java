package org.sda.mediaporter.services.tvShowServices.impl;

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

    @Autowired
    public TvShowServiceImpl(GenreService genreService, CountryService countryService, TvShowRepository tvShowRepository, FileService fileService, ContributorService contributorService, LanguageService languageService, CharacterService characterService) {
        this.genreService = genreService;
        this.countryService = countryService;
        this.tvShowRepository = tvShowRepository;
        this.fileService = fileService;
        this.contributorService = contributorService;
        this.languageService = languageService;
        this.characterService = characterService;
    }

    @Override
    public TvShow updateTvShowModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime) {
        tvShow.setLastModificationDateTime(modificationDateTime);
        return tvShowRepository.save(tvShow);
    }

    @Override
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
                TvShow tvShow =  tvShowRepository.save(toEntity(theMovieDbTvShowsById.getTheMovieDbTvShowDto(), writers, actors, directors, genres, originalLanguage, countries, theMovieDbTvShowId));
                List<Character> characters = characterService.createCharactersForTvShow(theMovieDbCreditsForTvShowById.getActors(), tvShow);
                tvShow.setCharacters(characters);
                return tvShowRepository.save(tvShow);
            }
        }
        return null;
    }

    @Override
    public void updateModificationDateTime(TvShow tvShow, LocalDateTime modificationDateTime) {
        tvShow.setLastModificationDateTime(modificationDateTime);
        tvShowRepository.save(tvShow);
    }

    private TvShow toEntity(TheMovieDbTvShowDto theMovieDbTvShowDto,List<Contributor> writers, List<Contributor> actors, List<Contributor> directors, List<Genre> genres, Language originalLanguage, List<Country> countries, Long theMovieDbTvShowId){
        System.out.println(theMovieDbTvShowDto.getTheMovieDbId());
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

    private TheMovieDbTvShowSearchDTO getTvShowAPISearchDTO(String searchTitle) {
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
