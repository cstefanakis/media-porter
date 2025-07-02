package org.sda.mediaporter.dataLoader;

import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.models.metadata.Resolution;
import org.sda.mediaporter.repositories.*;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.sda.mediaporter.repositories.metadata.ResolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final LanguageRepository languageRepository;
    private final GenreRepository genreRepository;
    private final ConfigurationRepository configurationRepository;
    private final CodecRepository codecRepository;
    private final ResolutionRepository resolutionRepository;
    private final SourcePathRepository sourcePathRepository;
    private final AudioChannelRepository audioChannelsRepository;

    @Autowired
    public DataLoader(LanguageRepository languageRepository, GenreRepository genreRepository, ConfigurationRepository configurationRepository, CodecRepository codecRepository, ResolutionRepository resolutionRepository, SourcePathRepository sourcePathRepository, AudioChannelRepository audioChannelsRepository) {
        this.languageRepository = languageRepository;
        this.genreRepository = genreRepository;
        this.configurationRepository = configurationRepository;
        this.codecRepository = codecRepository;
        this.resolutionRepository = resolutionRepository;
        this.sourcePathRepository = sourcePathRepository;
        this.audioChannelsRepository = audioChannelsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Codec data loader
        if(codecRepository.count() == 0){
            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("H264")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("X264")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("AVC")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("H265")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("AV1")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("VP8")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("VP9")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("MPEG2")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("MPEG4")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("XVID")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("DivX")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.VIDEO)
                    .name("HEVC")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("EAC3")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("AAC")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("MP3")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("FLAC")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("ALAC")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("OPUS")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("VORBIS")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("PCM")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("WMA")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("AC3")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.AUDIO)
                    .name("DTS")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.SUBTITLE)
                    .name("subrip")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.SUBTITLE)
                    .name("ass")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.SUBTITLE)
                    .name("ssa")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.SUBTITLE)
                    .name("movtext")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.SUBTITLE)
                    .name("webvtt")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.SUBTITLE)
                    .name("dvbsubtitle")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.SUBTITLE)
                    .name("hdmvpgssubtitle")
                    .build());

            codecRepository.save(Codec.builder()
                    .mediaType(MediaTypes.SUBTITLE)
                    .name("dvdsubtitle")
                    .build());

        }

        //Audio channels data loader
        if(audioChannelsRepository.count() == 0){
            audioChannelsRepository.save(AudioChannel.builder()
                    .id(1L)
                    .title("1 Mono")
                    .channels(1)
                    .description("Single audio channel")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .id(2L)
                    .title("2 Stereo")
                    .channels(2)
                    .description("Two-channel stereo sound")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .id(3L)
                    .title("2.1 Stereo + Sub")
                    .channels(3)
                    .description("Stereo with subwoofer")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .id(4L)
                    .title("4 Quadraphonic")
                    .channels(4)
                    .description("Four-channel surround")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .id(5L)
                    .title("5.1 Surround")
                    .channels(6)
                    .description("Six-channel surround (5 speakers + 1 sub)")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .id(6L)
                    .title("7.1 Full Surround")
                    .channels(8)
                    .description("Eight-channel surround")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .id(7L)
                    .title("7.1.2 Atmos Light")
                    .channels(10)
                    .description("Atmos configuration with height channels")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .id(8L)
                    .title("9.1.4 Atmos Advanced")
                    .channels(14)
                    .description("Advanced Atmos with additional speakers")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .id(9L)
                    .title("22.2 NHK Super Hi-Vision")
                    .channels(24)
                    .description("High-fidelity 22.2 surround format")
                    .build());
        }

        //Genres data loader
        if(genreRepository.count() == 0){
            genreRepository.save(Genre.builder()
                    .title("Action")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Adventure")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Animation")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Comedy")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Crime")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Documentary")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Drama")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Family")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Fantasy")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("History")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Horror")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Music")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Mystery")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Romance")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Sci-Fi")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("TV Movie")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Thriller")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("War")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Western")
                    .build());

        }

        //Languages data loader
        if(languageRepository.count() == 0){
            languageRepository.save(Language.builder().iso6391("en").iso6392B("eng").iso6392T("eng").englishTitle("English").originalTitle("English").build());
            languageRepository.save(Language.builder().iso6391("es").iso6392B("spa").iso6392T("spa").englishTitle("Spanish").originalTitle("Español").build());
            languageRepository.save(Language.builder().iso6391("fr").iso6392B("fre").iso6392T("fra").englishTitle("French").originalTitle("Français").build());
            languageRepository.save(Language.builder().iso6391("de").iso6392B("ger").iso6392T("deu").englishTitle("German").originalTitle("Deutsch").build());
            languageRepository.save(Language.builder().iso6391("it").iso6392B("ita").iso6392T("ita").englishTitle("Italian").originalTitle("Italiano").build());
            languageRepository.save(Language.builder().iso6391("pt").iso6392B("por").iso6392T("por").englishTitle("Portuguese").originalTitle("Português").build());
            languageRepository.save(Language.builder().iso6391("ru").iso6392B("rus").iso6392T("rus").englishTitle("Russian").originalTitle("Русский").build());
            languageRepository.save(Language.builder().iso6391("zh").iso6392B("chi").iso6392T("zho").englishTitle("Chinese").originalTitle("中文").build());
            languageRepository.save(Language.builder().iso6391("ja").iso6392B("jpn").iso6392T("jpn").englishTitle("Japanese").originalTitle("日本語").build());
            languageRepository.save(Language.builder().iso6391("ko").iso6392B("kor").iso6392T("kor").englishTitle("Korean").originalTitle("한국어").build());
            languageRepository.save(Language.builder().iso6391("ar").iso6392B("ara").iso6392T("ara").englishTitle("Arabic").originalTitle("العربية").build());
            languageRepository.save(Language.builder().iso6391("hi").iso6392B("hin").iso6392T("hin").englishTitle("Hindi").originalTitle("हिन्दी").build());
            languageRepository.save(Language.builder().iso6391("tr").iso6392B("tur").iso6392T("tur").englishTitle("Turkish").originalTitle("Türkçe").build());
            languageRepository.save(Language.builder().iso6391("pl").iso6392B("pol").iso6392T("pol").englishTitle("Polish").originalTitle("Polski").build());
            languageRepository.save(Language.builder().iso6391("nl").iso6392B("dut").iso6392T("nld").englishTitle("Dutch").originalTitle("Nederlands").build());
            languageRepository.save(Language.builder().iso6391("sv").iso6392B("swe").iso6392T("swe").englishTitle("Swedish").originalTitle("Svenska").build());
            languageRepository.save(Language.builder().iso6391("fi").iso6392B("fin").iso6392T("fin").englishTitle("Finnish").originalTitle("Suomi").build());
            languageRepository.save(Language.builder().iso6391("no").iso6392B("nor").iso6392T("nor").englishTitle("Norwegian").originalTitle("Norsk").build());
            languageRepository.save(Language.builder().iso6391("da").iso6392B("dan").iso6392T("dan").englishTitle("Danish").originalTitle("Dansk").build());
            languageRepository.save(Language.builder().iso6391("el").iso6392B("gre").iso6392T("ell").englishTitle("Greek").originalTitle("Ελληνικά").build());
            languageRepository.save(Language.builder().iso6391("cs").iso6392B("cze").iso6392T("ces").englishTitle("Czech").originalTitle("Čeština").build());
            languageRepository.save(Language.builder().iso6391("ro").iso6392B("rum").iso6392T("ron").englishTitle("Romanian").originalTitle("Română").build());
            languageRepository.save(Language.builder().iso6391("hu").iso6392B("hun").iso6392T("hun").englishTitle("Hungarian").originalTitle("Magyar").build());
            languageRepository.save(Language.builder().iso6391("th").iso6392B("tha").iso6392T("tha").englishTitle("Thai").originalTitle("ไทย").build());
            languageRepository.save(Language.builder().iso6391("id").iso6392B("ind").iso6392T("ind").englishTitle("Indonesian").originalTitle("Bahasa Indonesia").build());
            languageRepository.save(Language.builder().iso6391("he").iso6392B("heb").iso6392T("heb").englishTitle("Hebrew").originalTitle("עברית").build());
            languageRepository.save(Language.builder().iso6391("uk").iso6392B("ukr").iso6392T("ukr").englishTitle("Ukrainian").originalTitle("Українська").build());
            languageRepository.save(Language.builder().iso6391("vi").iso6392B("vie").iso6392T("vie").englishTitle("Vietnamese").originalTitle("Tiếng Việt").build());
            languageRepository.save(Language.builder().iso6391("ms").iso6392B("may").iso6392T("msa").englishTitle("Malay").originalTitle("Bahasa Melayu").build());
            languageRepository.save(Language.builder().iso6391("fa").iso6392B("per").iso6392T("fas").englishTitle("Persian").originalTitle("فارسی").build());
            languageRepository.save(Language.builder().iso6391("bn").iso6392B("ben").iso6392T("ben").englishTitle("Bengali").originalTitle("বাংলা").build());
            languageRepository.save(Language.builder().iso6391("sk").iso6392B("slo").iso6392T("slk").englishTitle("Slovak").originalTitle("Slovenčina").build());
            languageRepository.save(Language.builder().iso6391("hr").iso6392B("hrv").iso6392T("hrv").englishTitle("Croatian").originalTitle("Hrvatski").build());
            languageRepository.save(Language.builder().iso6391(null).iso6392B("fil").iso6392T("fil").englishTitle("Filipino").originalTitle("Filipino").build());
            languageRepository.save(Language.builder().iso6391("nb").iso6392B("nob").iso6392T("nob").englishTitle("Norwegian Bokmål").originalTitle("Bokmål").build());

        }

        //Resolutions data loader
        if(resolutionRepository.count() == 0){
            resolutionRepository.save(Resolution.builder().name("144p").build());
            resolutionRepository.save(Resolution.builder().name("240p").build());
            resolutionRepository.save(Resolution.builder().name("360p").build());
            resolutionRepository.save(Resolution.builder().name("480p").build());
            resolutionRepository.save(Resolution.builder().name("720p").build());
            resolutionRepository.save(Resolution.builder().name("1080p").build());
            resolutionRepository.save(Resolution.builder().name("1440p").build());
            resolutionRepository.save(Resolution.builder().name("4K").build());
            resolutionRepository.save(Resolution.builder().name("8K").build());
        }

        //Configuration data loader
        if(configurationRepository.count() == 0){
            configurationRepository.save(Configuration.builder()
                        .id(1L)
                        .maxDatesSaveFile(9000)
                        .maxDatesControlFilesFromExternalSource(0)
                        .videoResolutionsPrefer(resolutionRepository.findAll())
                        .firstVideoBitrateValueRange(0)
                        .secondVideoBitrateValueRange(200000000)
                        .firstAudioBitrateValueRange(0)
                        .secondAudioBitrateValueRange(2048000)
                        .firstAudioChannelsValueRange(0)
                        .secondAudioChannelsValueRange(24)
                        .firstVideoSizeRangeRange(0.0)
                        .secondVideoSizeRangeRange(31457280.0)
                            .audioCodecsPrefer(codecRepository.findByMediaType(MediaTypes.AUDIO))
                            .videoCodecsPrefer(codecRepository.findByMediaType(MediaTypes.VIDEO))
                    .build());
        }

        //Sources data loader
        if(sourcePathRepository.count() == 0){
            sourcePathRepository.save(SourcePath.builder()
                            .type(LibraryItems.MOVIE)
                            .path("Z:\\Downloads\\Movies")
                            .title("Movies Download Path")
                            .pathType(SourcePath.Type.DOWNLOAD)
                    .build());

            sourcePathRepository.save(SourcePath.builder()
                    .type(LibraryItems.MOVIE)
                    .path("Z:\\MultiMedia\\Movies")
                    .title("Movies Main Path")
                    .pathType(SourcePath.Type.SOURCE)
                    .build());
        }
    }
}
