package org.sda.mediaporter.testutil;

import jakarta.transaction.Transactional;
import org.sda.mediaporter.models.*;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.*;
import org.sda.mediaporter.models.metadata.Character;
import org.sda.mediaporter.repositories.*;
import org.sda.mediaporter.repositories.metadata.*;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AudioChannelRepository audioChannelsRepository;

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private SubtitleRepository subtitleRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;


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

    public Configuration createMovieDownloadsConfiguration(){
        return configurationRepository.save(Configuration.builder()
                .maxDatesSaveFile(5)
                .maxDatesControlFilesFromExternalSource(5000)
                //Video
                .videoCodecs(null)
                .videoResolutions(null)
                .firstVideoBitrateValueRange(null)
                .secondVideoSizeRange(null)
                //Audio
                .audioCodecs(null)
                .audioChannels(null)
                .audioLanguages(null)
                .firstAudioBitrateValueRange(null)
                .secondAudioBitrateValueRange(null)
                //Genres
                .genres(null)
                //file size range
                .firstVideoSizeRange(null)
                .secondVideoSizeRange(null)
                .build());
    }

    public SourcePath createSourcePathMovieDownloads(){
        String path = Path.of("src/test/resources/downloadSources/movies").normalize().toString();
        return sourcePathRepository.save(SourcePath.builder()
                        .title("Movies download source")
                        .path(path)
                        .pathType(SourcePath.PathType.DOWNLOAD)
                        .libraryItem(LibraryItems.MOVIE)
                        .configuration(createMovieDownloadsConfiguration())
                .build());
    }

    public Configuration createTvShowSourceConfiguration(){
        return configurationRepository.save(Configuration.builder()
                .maxDatesSaveFile(5)
                .maxDatesControlFilesFromExternalSource(5000)
                //Video
                .videoCodecs(null)
                .videoResolutions(null)
                .firstVideoBitrateValueRange(null)
                .secondVideoSizeRange(null)
                //Audio
                .audioCodecs(null)
                .audioChannels(null)
                .audioLanguages(null)
                .firstAudioBitrateValueRange(null)
                .secondAudioBitrateValueRange(null)
                //Genres
                .genres(null)
                //file size range
                .firstVideoSizeRange(null)
                .secondVideoSizeRange(null)
                .build());
    }

    public SourcePath createSourcePathTvShowSource(){
        String path = Path.of("src/test/resources/mainSources/tvShows").normalize().toString();
        return sourcePathRepository.save(SourcePath.builder()
                .path(path)
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.TV_SHOW)
                        .configuration(createTvShowSourceConfiguration())
                .build());
    }

    public Configuration createMovieSourceConfiguration(){
        return configurationRepository.save(Configuration.builder()
                .maxDatesSaveFile(5)
                .maxDatesControlFilesFromExternalSource(5000)
                //Video
                .videoCodecs(null)
                .videoResolutions(null)
                .firstVideoBitrateValueRange(null)
                .secondVideoSizeRange(null)
                //Audio
                .audioCodecs(null)
                .audioChannels(null)
                .audioLanguages(null)
                .firstAudioBitrateValueRange(null)
                .secondAudioBitrateValueRange(null)
                //Genres
                .genres(null)
                //file size range
                .firstVideoSizeRange(null)
                .secondVideoSizeRange(null)
                .build());
    }

    @Transactional
    public SourcePath createSourcePathMovieSource(){
        String path = Path.of("src/test/resources/mainSources/movies").normalize().toString();
        return sourcePathRepository.save(SourcePath.builder()
                .path(path)
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.TV_SHOW)
                        .configuration(createMovieSourceConfiguration())
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
    public VideoFilePath createTvShowVideoFilePath(){
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

    public User createUserUser(){
        return userRepository.save(User.builder()
                        .email("user@email.com")
                        .username("user")
                        .password("password")
                        .name("user name")
                .build());
    }

    public User createUserAdmin(){
        return userRepository.save(User.builder()
                .email("admin@email.com")
                .username("admin")
                .password("password")
                .name("admin name")
                .build());
    }

    public AudioChannel createAudioChannelMono(){
        return audioChannelsRepository.save(AudioChannel.builder()
                .title("1 Mono")
                .channels(1)
                .description("Single audio channel")
                .build());
    }

    public AudioChannel createAudioChannelStereo(){
        return audioChannelsRepository.save(AudioChannel.builder()
                .title("2 Stereo")
                .channels(2)
                .description("Two-channel stereo sound")
                .build());
    }

    public Codec createCodecH264(){
        return codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.VIDEO)
                .name("H264")
                .build());
    }

    public Codec createCodecH265(){
        return codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.VIDEO)
                .name("H265")
                .build());
    }

    private Codec createCodecSRT() {
        return codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.SUBTITLE)
                .name("SRT")
                .build());
    }

    public Codec createCodecAAC(){
        return codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("AAC")
                .build());
    }

    public Resolution createResolutionHd(){
        return resolutionRepository.save(Resolution.builder()
                .name("720p")
                .build());
    }

    public Resolution createResolutionFullHd(){
        return resolutionRepository.save(Resolution.builder()
                .name("1080p")
                .build());
    }

    @Transactional
    public Video createTvShowVideo(){
        return videoRepository.save(Video.builder()
                        .bitrate(1000)
                        .codec(createCodecH264())
                        .resolution(createResolutionFullHd())
                        .videoFilePath(createTvShowVideoFilePath())
                .build());
    }

    @Transactional
    public Audio createTvShowAudio(){
        return audioRepository.save(Audio.builder()
                        .audioChannel(createAudioChannelMono())
                        .bitrate(128)
                        .codec(createCodecAAC())
                        .language(createLanguageEn())
                        .videoFilePath(createTvShowVideoFilePath())
                .build());
    }

    @Transactional
    public Subtitle createTvShowSubtitle(){
        return subtitleRepository.save(Subtitle.builder()
                .codec(createCodecSRT())
                .language(createLanguageEn())
                .videoFilePath(createTvShowVideoFilePath())
                .build());
    }



    public Language createLanguageEn(){
        return languageRepository.save(Language.builder()
                .englishTitle("English")
                .originalTitle("English")
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .build());
    }

    @Transactional
    public Movie createMovie(){
        List<Genre> genres = new ArrayList<>();
        genres.add(createGenreAdventure());
        genres.add(createGenreAction());

        List<Contributor> actors = new ArrayList<>();
        actors.add(createActor());

        List<Contributor> writers = new ArrayList<>();
        writers.add(createWriter());

        List<Contributor> directors = new ArrayList<>();
        directors.add(createDirector());

        List<Country> countries = new ArrayList<>();
        countries.add(createCountryAlbania());

        List<Character> characters = new ArrayList<>();

        return movieRepository.save(Movie.builder()
                .title("Test Movie")
                .originalTitle("Test Movie Original")
                .year(2026)
                .rate(8.5)
                .releaseDate(LocalDate.of(2026, 2, 2))
                .poster("test_poster.jpg")
                .overview("This is a test movie for unit testing.")
                .lastModificationDateTime(LocalDateTime.now())
                .theMovieDbId(123456L)
                .originalLanguage(createLanguageEn())
                .genres(genres)
                .directors(directors)
                .writers(writers)
                .actors(actors)
                .countries(countries)
                .characters(characters)
                .build());
    }

    @Transactional
    public VideoFilePath createMovieVideoFilePath(){
        TvShowEpisode tvShowEpisode = createTvShowEpisode();
        SourcePath sourcePath = createSourcePathMovieSource();
        String path = Path.of("/movieFile.mp4").normalize().toString();
        VideoFilePath videoFilePath = videoFilePathRepository.save(VideoFilePath.builder()
                .filePath(path)
                        .movie(createMovie())
                .modificationDateTime(tvShowEpisode.getModificationDateTime())
                .sourcePath(sourcePath)
                .build());

        createMovieVideo();
        createMovieAudio();
        createMovieSubtitle();

        return videoFilePath;
    }

    @Transactional
    public Video createMovieVideo(){
        return videoRepository.save(Video.builder()
                .bitrate(1000)
                .codec(createCodecH264())
                .resolution(createResolutionFullHd())
                .videoFilePath(createMovieVideoFilePath())
                .build());
    }

    @Transactional
    public Audio createMovieAudio(){
        return audioRepository.save(Audio.builder()
                .audioChannel(createAudioChannelMono())
                .bitrate(128)
                .codec(createCodecAAC())
                .language(createLanguageEn())
                .videoFilePath(createMovieVideoFilePath())
                .build());
    }

    @Transactional
    public Subtitle createMovieSubtitle(){
        return subtitleRepository.save(Subtitle.builder()
                .codec(createCodecSRT())
                .language(createLanguageEn())
                .videoFilePath(createMovieVideoFilePath())
                .build());
    }

    //Genres
    public Genre createGenreAction(){
        return genreRepository.save(Genre.builder()
                .title("Action")
                .build());
    }

    public Genre createGenreAdventure(){
        return genreRepository.save(Genre.builder()
                .title("Adventure")
                .build());
    }
}
