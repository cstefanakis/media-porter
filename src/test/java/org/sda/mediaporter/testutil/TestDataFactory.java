package org.sda.mediaporter.testutil;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.metadata.Video;
import org.sda.mediaporter.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class TestDataFactory {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ContributorRepository contributorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private TvShowRepository tvShowRepository;

    @Autowired
    private TvShowEpisodeRepository tvShowEpisodeRepository;

    @Autowired
    private VideoFilePathRepository videoFilePathRepository;

    public Video createVideoTvShow(){
        return null;
    }

    public Movie createTestMovie() {
        // Language
        Language language = languageRepository.save(Language.builder()
                .englishTitle("English")
                .originalTitle("English")
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .build()
        );
        // Genres
        Genre genre1 = genreRepository.save(Genre.builder().title("Action").build());
        Genre genre2 = genreRepository.save(Genre.builder().title("Adventure").build());

        // Contributors (Director, Writer, Actor)
        Contributor director = contributorRepository.save(Contributor.builder().fullName("John Doe").build());
        Contributor writer = contributorRepository.save(Contributor.builder().fullName("Jane Smith").build());
        Contributor actor = contributorRepository.save(Contributor.builder().fullName("Chris Example").build());

        // Build movie
        Movie movie = movieRepository.save(Movie.builder()
                .title("Test Movie")
                .originalTitle("Test Movie Original")
                .year(2026)
                .rate(8.5)
                .releaseDate(LocalDate.of(2026, 2, 2))
                .poster("test_poster.jpg")
                .overview("This is a test movie for unit testing.")
                .lastModificationDateTime(LocalDateTime.now())
                .theMovieDbId(123456L)
                .originalLanguage(language)
                .genres(List.of(genre1, genre2))
                .directors(List.of(director))
                .writers(List.of(writer))
                .actors(List.of(actor))
                .countries(List.of())
                .characters(List.of())
                .videoFilePaths(List.of())
                .build());

        // Link genres back to movie (optional for bidirectional mapping)
        genre1.setMovieGenres(List.of(movie));
        genre2.setMovieGenres(List.of(movie));
        genreRepository.save(genre1);
        genreRepository.save(genre2);

        // Link contributors back to movie (optional)
        director.setMovieDirectors(List.of(movie));
        contributorRepository.save(director);

        writer.setMovieWriters(List.of(movie));
        contributorRepository.save(writer);

        actor.setMovieActors(List.of(movie));
        contributorRepository.save(actor);

        return movie;
    }

    public Contributor createDirector(){
        return contributorRepository.save(Contributor.builder()
                .fullName("John Doe")
                        .theMovieDbId(128L)
                .build());
    }

    public Contributor createWriter(){
        return contributorRepository.save(Contributor.builder()
                .fullName("Jane Smith")
                        .theMovieDbId(129L)
                .build());
    }

    public Contributor createActor(){
        return contributorRepository.save(Contributor.builder()
                .fullName("Chris Example")
                        .theMovieDbId(130L)
                .build());
    }

    public Country createCountryAfghanistan() {
        return countryRepository.save(Country.builder()
                .iso2Code("AF")
                .iso3Code("AFG")
                .englishName("Afghanistan")
                .nativeName("Afghanistan").build());
    }

    public Country createCountryAlbania(){
        return countryRepository.save(Country.builder()
                .iso2Code("AL")
                .iso3Code("ALB")
                .englishName("Albania")
                .nativeName("Albania").build());
    }

    public Gender createGenderMale() {
        return genderRepository.save(Gender.builder()
                .title("male")
                .build());
    }
    public Gender createGenderFemale() {
        return genderRepository.save(Gender.builder()
                .title("female")
                .build());
    }

    public Role createRoleAdministrator(){
        return roleRepository.save(Role.builder()
                        .name("Administrator")
                .build());
    }

    public Role createRoleUser(){
        return roleRepository.save(Role.builder()
                .name("User")
                .build());
    }

    public SourcePath createSourcePathMovieDownloads(){
        String path = Path.of("src/test/resources/downloadSources/movies").normalize().toString();
        return sourcePathRepository.save(SourcePath.builder()
                        .title("Movies download source")
                        .path(path)
                        .pathType(SourcePath.PathType.DOWNLOAD)
                        .libraryItem(LibraryItems.MOVIE)
                .build());
    }

    public SourcePath createSourcePathTvShowSource(){
        String path = Path.of("c:/tvShow").normalize().toString();
        return sourcePathRepository.save(SourcePath.builder()
                .path(path)
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.TV_SHOW)
                .build());
    }

    public TvShow createTvShow(){
        LocalDateTime daysBefore2 = LocalDateTime.now().minusDays(2);
        return tvShowRepository.save(TvShow.builder()
                .title("tvShowTitle1")
                        .year(2025)
                        .theMoveDBTvShowId(20L)
                .lastModificationDateTime(daysBefore2)
                .build());
    }

    @Transactional
    public TvShowEpisode createTvShowEpisode(){
        TvShow tvShow = createTvShow();
        return tvShowEpisodeRepository.save(TvShowEpisode.builder()
                .episodeName("title1")
                .theMovieDbId(1L)
                .seasonNumber(1)
                .episodeNumber(1)
                        .tvShow(tvShow)
                        .modificationDateTime(tvShow.getLastModificationDateTime())
                .build());
    }

    @Transactional
    public VideoFilePath createVideoFilePath(){
        TvShowEpisode tvShowEpisode = createTvShowEpisode();
        SourcePath sourcePath = createSourcePathTvShowSource();
        String path = Path.of("/test1.mp4").normalize().toString();
        return videoFilePathRepository.save(VideoFilePath.builder()
                .filePath(path)
                        .tvShowEpisode(tvShowEpisode)
                        .modificationDateTime(tvShowEpisode.getModificationDateTime())
                        .sourcePath(sourcePath)
                .build());
    }

    public TvShow createTvShowWithoutVideoFilePaths(){
        LocalDateTime daysBefore2 = LocalDateTime.now().minusDays(4);
        return tvShowRepository.save(TvShow.builder()
                .title("tvShowTitle2")
                .year(2025)
                .theMoveDBTvShowId(21L)
                .lastModificationDateTime(daysBefore2)
                .build());
    }

    @Transactional
    public TvShowEpisode createTvShowEpisodeWithoutVideoFilePath(){
        TvShow tvShow = createTvShowWithoutVideoFilePaths();
        return tvShowEpisodeRepository.save(TvShowEpisode.builder()
                .episodeName("title2")
                .theMovieDbId(2L)
                .seasonNumber(1)
                .episodeNumber(1)
                .tvShow(tvShow)
                .modificationDateTime(tvShow.getLastModificationDateTime())
                        .videoFilePaths(new ArrayList<>())
                .build());
    }
}
