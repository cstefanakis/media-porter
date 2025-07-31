package org.sda.mediaporter.dataLoader;

import jakarta.persistence.EntityNotFoundException;
import org.sda.mediaporter.config.SpringSecurityConfig;
import org.sda.mediaporter.models.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final LanguageRepository languageRepository;
    private final GenreRepository genreRepository;
    private final ConfigurationRepository configurationRepository;
    private final CodecRepository codecRepository;
    private final ResolutionRepository resolutionRepository;
    private final SourcePathRepository sourcePathRepository;
    private final AudioChannelRepository audioChannelsRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public DataLoader(LanguageRepository languageRepository, GenreRepository genreRepository, ConfigurationRepository configurationRepository, CodecRepository codecRepository, ResolutionRepository resolutionRepository, SourcePathRepository sourcePathRepository, AudioChannelRepository audioChannelsRepository, CountryRepository countryRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.languageRepository = languageRepository;
        this.genreRepository = genreRepository;
        this.configurationRepository = configurationRepository;
        this.codecRepository = codecRepository;
        this.resolutionRepository = resolutionRepository;
        this.sourcePathRepository = sourcePathRepository;
        this.audioChannelsRepository = audioChannelsRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Role data loader
        if(roleRepository.count() == 0){
            roleRepository.save(Role.builder()
                            .name("ROLE_ADMIN")
                    .build());
            roleRepository.save(Role.builder()
                            .name("ROLE_USER")
                    .build());
        }
        //User data loader
        if(userRepository.count() == 0){
            
            Role admin = roleRepository.findByName("ROLE_ADMIN").orElseThrow(()-> new EntityNotFoundException("Role not found"));
            Role user = roleRepository.findByName("ROLE_USER").orElseThrow(()-> new EntityNotFoundException("Role not found"));

            userRepository.save(User.builder()
                            .email("admin@gmail.com")
                            .username("admin")
                            .password(new BCryptPasswordEncoder().encode("admin"))
                            .roles(Set.of(user, admin))
                    .build());

            userRepository.save(User.builder()
                    .email("user@gmail.com")
                    .username("user")
                    .password(new BCryptPasswordEncoder().encode("user"))
                    .roles(Set.of(user))
                    .build());

        }

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
                    .title("1 Mono")
                    .channels(1)
                    .description("Single audio channel")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .title("2 Stereo")
                    .channels(2)
                    .description("Two-channel stereo sound")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .title("2.1 Stereo + Sub")
                    .channels(3)
                    .description("Stereo with subwoofer")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .title("4 Quadraphonic")
                    .channels(4)
                    .description("Four-channel surround")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .title("5.1 Surround")
                    .channels(6)
                    .description("Six-channel surround (5 speakers + 1 sub)")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .title("7.1 Full Surround")
                    .channels(8)
                    .description("Eight-channel surround")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .title("7.1.2 Atmos Light")
                    .channels(10)
                    .description("Atmos configuration with height channels")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
                    .title("9.1.4 Atmos Advanced")
                    .channels(14)
                    .description("Advanced Atmos with additional speakers")
                    .build());

            audioChannelsRepository.save(AudioChannel.builder()
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

            genreRepository.save(Genre.builder()
                    .title("Sport")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Biography")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("Short")
                    .build());

            genreRepository.save(Genre.builder()
                    .title("N/A")
                    .build());
        }

        //Country data loader
        if(countryRepository.count() == 0) {
            countryRepository.save(Country.builder().iso2Code("AF").iso3Code("AFG").englishName("Afghanistan").nativeName("Afghanistan").build());
            countryRepository.save(Country.builder().iso2Code("AL").iso3Code("ALB").englishName("Albania").nativeName("Albania").build());
            countryRepository.save(Country.builder().iso2Code("DZ").iso3Code("DZA").englishName("Algeria").nativeName("Algeria").build());
            countryRepository.save(Country.builder().iso2Code("AD").iso3Code("AND").englishName("Andorra").nativeName("Andorra").build());
            countryRepository.save(Country.builder().iso2Code("AO").iso3Code("AGO").englishName("Angola").nativeName("Angola").build());
            countryRepository.save(Country.builder().iso2Code("AG").iso3Code("ATG").englishName("Antigua and Barbuda").nativeName("Antigua and Barbuda").build());
            countryRepository.save(Country.builder().iso2Code("AR").iso3Code("ARG").englishName("Argentina").nativeName("Argentina").build());
            countryRepository.save(Country.builder().iso2Code("AM").iso3Code("ARM").englishName("Armenia").nativeName("Armenia").build());
            countryRepository.save(Country.builder().iso2Code("AU").iso3Code("AUS").englishName("Australia").nativeName("Australia").build());
            countryRepository.save(Country.builder().iso2Code("AT").iso3Code("AUT").englishName("Austria").nativeName("Austria").build());
            countryRepository.save(Country.builder().iso2Code("AZ").iso3Code("AZE").englishName("Azerbaijan").nativeName("Azerbaijan").build());
            countryRepository.save(Country.builder().iso2Code("BS").iso3Code("BHS").englishName("Bahamas").nativeName("Bahamas").build());
            countryRepository.save(Country.builder().iso2Code("BH").iso3Code("BHR").englishName("Bahrain").nativeName("Bahrain").build());
            countryRepository.save(Country.builder().iso2Code("BD").iso3Code("BGD").englishName("Bangladesh").nativeName("Bangladesh").build());
            countryRepository.save(Country.builder().iso2Code("BB").iso3Code("BRB").englishName("Barbados").nativeName("Barbados").build());
            countryRepository.save(Country.builder().iso2Code("BY").iso3Code("BLR").englishName("Belarus").nativeName("Belarus").build());
            countryRepository.save(Country.builder().iso2Code("BE").iso3Code("BEL").englishName("Belgium").nativeName("Belgium").build());
            countryRepository.save(Country.builder().iso2Code("BZ").iso3Code("BLZ").englishName("Belize").nativeName("Belize").build());
            countryRepository.save(Country.builder().iso2Code("BJ").iso3Code("BEN").englishName("Benin").nativeName("Benin").build());
            countryRepository.save(Country.builder().iso2Code("BT").iso3Code("BTN").englishName("Bhutan").nativeName("Bhutan").build());
            countryRepository.save(Country.builder().iso2Code("BO").iso3Code("BOL").englishName("Bolivia").nativeName("Bolivia").build());
            countryRepository.save(Country.builder().iso2Code("BA").iso3Code("BIH").englishName("Bosnia and Herzegovina").nativeName("Bosnia and Herzegovina").build());
            countryRepository.save(Country.builder().iso2Code("BW").iso3Code("BWA").englishName("Botswana").nativeName("Botswana").build());
            countryRepository.save(Country.builder().iso2Code("BR").iso3Code("BRA").englishName("Brazil").nativeName("Brazil").build());
            countryRepository.save(Country.builder().iso2Code("BN").iso3Code("BRN").englishName("Brunei").nativeName("Brunei").build());
            countryRepository.save(Country.builder().iso2Code("BG").iso3Code("BGR").englishName("Bulgaria").nativeName("Bulgaria").build());
            countryRepository.save(Country.builder().iso2Code("BF").iso3Code("BFA").englishName("Burkina Faso").nativeName("Burkina Faso").build());
            countryRepository.save(Country.builder().iso2Code("BI").iso3Code("BDI").englishName("Burundi").nativeName("Burundi").build());
            countryRepository.save(Country.builder().iso2Code("CV").iso3Code("CPV").englishName("Cabo Verde").nativeName("Cabo Verde").build());
            countryRepository.save(Country.builder().iso2Code("KH").iso3Code("KHM").englishName("Cambodia").nativeName("Cambodia").build());
            countryRepository.save(Country.builder().iso2Code("CM").iso3Code("CMR").englishName("Cameroon").nativeName("Cameroon").build());
            countryRepository.save(Country.builder().iso2Code("CA").iso3Code("CAN").englishName("Canada").nativeName("Canada").build());
            countryRepository.save(Country.builder().iso2Code("CF").iso3Code("CAF").englishName("Central African Republic").nativeName("Central African Republic").build());
            countryRepository.save(Country.builder().iso2Code("TD").iso3Code("TCD").englishName("Chad").nativeName("Chad").build());
            countryRepository.save(Country.builder().iso2Code("CL").iso3Code("CHL").englishName("Chile").nativeName("Chile").build());
            countryRepository.save(Country.builder().iso2Code("CN").iso3Code("CHN").englishName("China").nativeName("China").build());
            countryRepository.save(Country.builder().iso2Code("CO").iso3Code("COL").englishName("Colombia").nativeName("Colombia").build());
            countryRepository.save(Country.builder().iso2Code("KM").iso3Code("COM").englishName("Comoros").nativeName("Comoros").build());
            countryRepository.save(Country.builder().iso2Code("CG").iso3Code("COG").englishName("Congo").nativeName("Congo").build());
            countryRepository.save(Country.builder().iso2Code("CR").iso3Code("CRI").englishName("Costa Rica").nativeName("Costa Rica").build());
            countryRepository.save(Country.builder().iso2Code("CI").iso3Code("CIV").englishName("Côte d'Ivoire").nativeName("Côte d'Ivoire").build());
            countryRepository.save(Country.builder().iso2Code("HR").iso3Code("HRV").englishName("Croatia").nativeName("Croatia").build());
            countryRepository.save(Country.builder().iso2Code("CU").iso3Code("CUB").englishName("Cuba").nativeName("Cuba").build());
            countryRepository.save(Country.builder().iso2Code("CY").iso3Code("CYP").englishName("Cyprus").nativeName("Cyprus").build());
            countryRepository.save(Country.builder().iso2Code("CZ").iso3Code("CZE").englishName("Czech Republic").nativeName("Czechia").build());
            countryRepository.save(Country.builder().iso2Code("CD").iso3Code("COD").englishName("Democratic Republic of the Congo").nativeName("Democratic Republic of the Congo").build());
            countryRepository.save(Country.builder().iso2Code("DK").iso3Code("DNK").englishName("Denmark").nativeName("Denmark").build());
            countryRepository.save(Country.builder().iso2Code("DJ").iso3Code("DJI").englishName("Djibouti").nativeName("Djibouti").build());
            countryRepository.save(Country.builder().iso2Code("DM").iso3Code("DMA").englishName("Dominica").nativeName("Dominica").build());
            countryRepository.save(Country.builder().iso2Code("DO").iso3Code("DOM").englishName("Dominican Republic").nativeName("Dominican Republic").build());
            countryRepository.save(Country.builder().iso2Code("EC").iso3Code("ECU").englishName("Ecuador").nativeName("Ecuador").build());
            countryRepository.save(Country.builder().iso2Code("EG").iso3Code("EGY").englishName("Egypt").nativeName("Egypt").build());
            countryRepository.save(Country.builder().iso2Code("SV").iso3Code("SLV").englishName("El Salvador").nativeName("El Salvador").build());
            countryRepository.save(Country.builder().iso2Code("GQ").iso3Code("GNQ").englishName("Equatorial Guinea").nativeName("Equatorial Guinea").build());
            countryRepository.save(Country.builder().iso2Code("ER").iso3Code("ERI").englishName("Eritrea").nativeName("Eritrea").build());
            countryRepository.save(Country.builder().iso2Code("EE").iso3Code("EST").englishName("Estonia").nativeName("Estonia").build());
            countryRepository.save(Country.builder().iso2Code("SZ").iso3Code("SWZ").englishName("Eswatini").nativeName("Eswatini").build());
            countryRepository.save(Country.builder().iso2Code("ET").iso3Code("ETH").englishName("Ethiopia").nativeName("Ethiopia").build());
            countryRepository.save(Country.builder().iso2Code("FJ").iso3Code("FJI").englishName("Fiji").nativeName("Fiji").build());
            countryRepository.save(Country.builder().iso2Code("FI").iso3Code("FIN").englishName("Finland").nativeName("Finland").build());
            countryRepository.save(Country.builder().iso2Code("FR").iso3Code("FRA").englishName("France").nativeName("France").build());
            countryRepository.save(Country.builder().iso2Code("GA").iso3Code("GAB").englishName("Gabon").nativeName("Gabon").build());
            countryRepository.save(Country.builder().iso2Code("GM").iso3Code("GMB").englishName("Gambia").nativeName("Gambia").build());
            countryRepository.save(Country.builder().iso2Code("GE").iso3Code("GEO").englishName("Georgia").nativeName("Georgia").build());
            countryRepository.save(Country.builder().iso2Code("DE").iso3Code("DEU").englishName("Germany").nativeName("Germany").build());
            countryRepository.save(Country.builder().iso2Code("GH").iso3Code("GHA").englishName("Ghana").nativeName("Ghana").build());
            countryRepository.save(Country.builder().iso2Code("GR").iso3Code("GRC").englishName("Greece").nativeName("Greece").build());
            countryRepository.save(Country.builder().iso2Code("GD").iso3Code("GRD").englishName("Grenada").nativeName("Grenada").build());
            countryRepository.save(Country.builder().iso2Code("GT").iso3Code("GTM").englishName("Guatemala").nativeName("Guatemala").build());
            countryRepository.save(Country.builder().iso2Code("GN").iso3Code("GIN").englishName("Guinea").nativeName("Guinea").build());
            countryRepository.save(Country.builder().iso2Code("GW").iso3Code("GNB").englishName("Guinea-Bissau").nativeName("Guinea-Bissau").build());
            countryRepository.save(Country.builder().iso2Code("GY").iso3Code("GUY").englishName("Guyana").nativeName("Guyana").build());
            countryRepository.save(Country.builder().iso2Code("HT").iso3Code("HTI").englishName("Haiti").nativeName("Haiti").build());
            countryRepository.save(Country.builder().iso2Code("HN").iso3Code("HND").englishName("Honduras").nativeName("Honduras").build());
            countryRepository.save(Country.builder().iso2Code("HU").iso3Code("HUN").englishName("Hungary").nativeName("Hungary").build());
            countryRepository.save(Country.builder().iso2Code("IS").iso3Code("ISL").englishName("Iceland").nativeName("Iceland").build());
            countryRepository.save(Country.builder().iso2Code("IN").iso3Code("IND").englishName("India").nativeName("India").build());
            countryRepository.save(Country.builder().iso2Code("ID").iso3Code("IDN").englishName("Indonesia").nativeName("Indonesia").build());
            countryRepository.save(Country.builder().iso2Code("IR").iso3Code("IRN").englishName("Iran").nativeName("Iran").build());
            countryRepository.save(Country.builder().iso2Code("IQ").iso3Code("IRQ").englishName("Iraq").nativeName("Iraq").build());
            countryRepository.save(Country.builder().iso2Code("IE").iso3Code("IRL").englishName("Ireland").nativeName("Ireland").build());
            countryRepository.save(Country.builder().iso2Code("IL").iso3Code("ISR").englishName("Israel").nativeName("Israel").build());
            countryRepository.save(Country.builder().iso2Code("IT").iso3Code("ITA").englishName("Italy").nativeName("Italy").build());
            countryRepository.save(Country.builder().iso2Code("JM").iso3Code("JAM").englishName("Jamaica").nativeName("Jamaica").build());
            countryRepository.save(Country.builder().iso2Code("JP").iso3Code("JPN").englishName("Japan").nativeName("Japan").build());
            countryRepository.save(Country.builder().iso2Code("JO").iso3Code("JOR").englishName("Jordan").nativeName("Jordan").build());
            countryRepository.save(Country.builder().iso2Code("KZ").iso3Code("KAZ").englishName("Kazakhstan").nativeName("Kazakhstan").build());
            countryRepository.save(Country.builder().iso2Code("KE").iso3Code("KEN").englishName("Kenya").nativeName("Kenya").build());
            countryRepository.save(Country.builder().iso2Code("KI").iso3Code("KIR").englishName("Kiribati").nativeName("Kiribati").build());
            countryRepository.save(Country.builder().iso2Code("KW").iso3Code("KWT").englishName("Kuwait").nativeName("Kuwait").build());
            countryRepository.save(Country.builder().iso2Code("KG").iso3Code("KGZ").englishName("Kyrgyzstan").nativeName("Kyrgyzstan").build());
            countryRepository.save(Country.builder().iso2Code("LA").iso3Code("LAO").englishName("Laos").nativeName("Laos").build());
            countryRepository.save(Country.builder().iso2Code("LV").iso3Code("LVA").englishName("Latvia").nativeName("Latvia").build());
            countryRepository.save(Country.builder().iso2Code("LB").iso3Code("LBN").englishName("Lebanon").nativeName("Lebanon").build());
            countryRepository.save(Country.builder().iso2Code("LS").iso3Code("LSO").englishName("Lesotho").nativeName("Lesotho").build());
            countryRepository.save(Country.builder().iso2Code("LR").iso3Code("LBR").englishName("Liberia").nativeName("Liberia").build());
            countryRepository.save(Country.builder().iso2Code("LY").iso3Code("LBY").englishName("Libya").nativeName("Libya").build());
            countryRepository.save(Country.builder().iso2Code("LI").iso3Code("LIE").englishName("Liechtenstein").nativeName("Liechtenstein").build());
            countryRepository.save(Country.builder().iso2Code("LT").iso3Code("LTU").englishName("Lithuania").nativeName("Lithuania").build());
            countryRepository.save(Country.builder().iso2Code("LU").iso3Code("LUX").englishName("Luxembourg").nativeName("Luxembourg").build());
            countryRepository.save(Country.builder().iso2Code("MG").iso3Code("MDG").englishName("Madagascar").nativeName("Madagascar").build());
            countryRepository.save(Country.builder().iso2Code("MW").iso3Code("MWI").englishName("Malawi").nativeName("Malawi").build());
            countryRepository.save(Country.builder().iso2Code("MY").iso3Code("MYS").englishName("Malaysia").nativeName("Malaysia").build());
            countryRepository.save(Country.builder().iso2Code("MV").iso3Code("MDV").englishName("Maldives").nativeName("Maldives").build());
            countryRepository.save(Country.builder().iso2Code("ML").iso3Code("MLI").englishName("Mali").nativeName("Mali").build());
            countryRepository.save(Country.builder().iso2Code("MT").iso3Code("MLT").englishName("Malta").nativeName("Malta").build());
            countryRepository.save(Country.builder().iso2Code("MH").iso3Code("MHL").englishName("Marshall Islands").nativeName("Marshall Islands").build());
            countryRepository.save(Country.builder().iso2Code("MR").iso3Code("MRT").englishName("Mauritania").nativeName("Mauritania").build());
            countryRepository.save(Country.builder().iso2Code("MU").iso3Code("MUS").englishName("Mauritius").nativeName("Mauritius").build());
            countryRepository.save(Country.builder().iso2Code("MX").iso3Code("MEX").englishName("Mexico").nativeName("Mexico").build());
            countryRepository.save(Country.builder().iso2Code("FM").iso3Code("FSM").englishName("Micronesia").nativeName("Micronesia").build());
            countryRepository.save(Country.builder().iso2Code("MD").iso3Code("MDA").englishName("Moldova").nativeName("Moldova").build());
            countryRepository.save(Country.builder().iso2Code("MC").iso3Code("MCO").englishName("Monaco").nativeName("Monaco").build());
            countryRepository.save(Country.builder().iso2Code("MN").iso3Code("MNG").englishName("Mongolia").nativeName("Mongolia").build());
            countryRepository.save(Country.builder().iso2Code("ME").iso3Code("MNE").englishName("Montenegro").nativeName("Montenegro").build());
            countryRepository.save(Country.builder().iso2Code("MA").iso3Code("MAR").englishName("Morocco").nativeName("Morocco").build());
            countryRepository.save(Country.builder().iso2Code("MZ").iso3Code("MOZ").englishName("Mozambique").nativeName("Mozambique").build());
            countryRepository.save(Country.builder().iso2Code("MM").iso3Code("MMR").englishName("Myanmar").nativeName("Myanmar").build());
            countryRepository.save(Country.builder().iso2Code("NA").iso3Code("NAM").englishName("Namibia").nativeName("Namibia").build());
            countryRepository.save(Country.builder().iso2Code("NR").iso3Code("NRU").englishName("Nauru").nativeName("Nauru").build());
            countryRepository.save(Country.builder().iso2Code("NP").iso3Code("NPL").englishName("Nepal").nativeName("Nepal").build());
            countryRepository.save(Country.builder().iso2Code("NL").iso3Code("NLD").englishName("Netherlands").nativeName("Netherlands").build());
            countryRepository.save(Country.builder().iso2Code("NZ").iso3Code("NZL").englishName("New Zealand").nativeName("New Zealand").build());
            countryRepository.save(Country.builder().iso2Code("NI").iso3Code("NIC").englishName("Nicaragua").nativeName("Nicaragua").build());
            countryRepository.save(Country.builder().iso2Code("NE").iso3Code("NER").englishName("Niger").nativeName("Niger").build());
            countryRepository.save(Country.builder().iso2Code("NG").iso3Code("NGA").englishName("Nigeria").nativeName("Nigeria").build());
            countryRepository.save(Country.builder().iso2Code("KP").iso3Code("PRK").englishName("North Korea").nativeName("North Korea").build());
            countryRepository.save(Country.builder().iso2Code("MK").iso3Code("MKD").englishName("North Macedonia").nativeName("North Macedonia").build());
            countryRepository.save(Country.builder().iso2Code("NO").iso3Code("NOR").englishName("Norway").nativeName("Norway").build());
            countryRepository.save(Country.builder().iso2Code("OM").iso3Code("OMN").englishName("Oman").nativeName("Oman").build());
            countryRepository.save(Country.builder().iso2Code("PK").iso3Code("PAK").englishName("Pakistan").nativeName("Pakistan").build());
            countryRepository.save(Country.builder().iso2Code("PW").iso3Code("PLW").englishName("Palau").nativeName("Palau").build());
            countryRepository.save(Country.builder().iso2Code("PA").iso3Code("PAN").englishName("Panama").nativeName("Panama").build());
            countryRepository.save(Country.builder().iso2Code("PG").iso3Code("PNG").englishName("Papua New Guinea").nativeName("Papua New Guinea").build());
            countryRepository.save(Country.builder().iso2Code("PY").iso3Code("PRY").englishName("Paraguay").nativeName("Paraguay").build());
            countryRepository.save(Country.builder().iso2Code("PE").iso3Code("PER").englishName("Peru").nativeName("Peru").build());
            countryRepository.save(Country.builder().iso2Code("PH").iso3Code("PHL").englishName("Philippines").nativeName("Philippines").build());
            countryRepository.save(Country.builder().iso2Code("PL").iso3Code("POL").englishName("Poland").nativeName("Poland").build());
            countryRepository.save(Country.builder().iso2Code("PT").iso3Code("PRT").englishName("Portugal").nativeName("Portugal").build());
            countryRepository.save(Country.builder().iso2Code("QA").iso3Code("QAT").englishName("Qatar").nativeName("Qatar").build());
            countryRepository.save(Country.builder().iso2Code("RO").iso3Code("ROU").englishName("Romania").nativeName("Romania").build());
            countryRepository.save(Country.builder().iso2Code("RU").iso3Code("RUS").englishName("Russia").nativeName("Russia").build());
            countryRepository.save(Country.builder().iso2Code("RW").iso3Code("RWA").englishName("Rwanda").nativeName("Rwanda").build());
            countryRepository.save(Country.builder().iso2Code("KN").iso3Code("KNA").englishName("Saint Kitts and Nevis").nativeName("Saint Kitts and Nevis").build());
            countryRepository.save(Country.builder().iso2Code("LC").iso3Code("LCA").englishName("Saint Lucia").nativeName("Saint Lucia").build());
            countryRepository.save(Country.builder().iso2Code("VC").iso3Code("VCT").englishName("Saint Vincent and the Grenadines").nativeName("Saint Vincent and the Grenadines").build());
            countryRepository.save(Country.builder().iso2Code("WS").iso3Code("WSM").englishName("Samoa").nativeName("Samoa").build());
            countryRepository.save(Country.builder().iso2Code("SM").iso3Code("SMR").englishName("San Marino").nativeName("San Marino").build());
            countryRepository.save(Country.builder().iso2Code("ST").iso3Code("STP").englishName("Sao Tome and Principe").nativeName("Sao Tome and Principe").build());
            countryRepository.save(Country.builder().iso2Code("SA").iso3Code("SAU").englishName("Saudi Arabia").nativeName("Saudi Arabia").build());
            countryRepository.save(Country.builder().iso2Code("SN").iso3Code("SEN").englishName("Senegal").nativeName("Senegal").build());
            countryRepository.save(Country.builder().iso2Code("RS").iso3Code("SRB").englishName("Serbia").nativeName("Serbia").build());
            countryRepository.save(Country.builder().iso2Code("SC").iso3Code("SYC").englishName("Seychelles").nativeName("Seychelles").build());
            countryRepository.save(Country.builder().iso2Code("SL").iso3Code("SLE").englishName("Sierra Leone").nativeName("Sierra Leone").build());
            countryRepository.save(Country.builder().iso2Code("SG").iso3Code("SGP").englishName("Singapore").nativeName("Singapore").build());
            countryRepository.save(Country.builder().iso2Code("SK").iso3Code("SVK").englishName("Slovakia").nativeName("Slovakia").build());
            countryRepository.save(Country.builder().iso2Code("SI").iso3Code("SVN").englishName("Slovenia").nativeName("Slovenia").build());
            countryRepository.save(Country.builder().iso2Code("SB").iso3Code("SLB").englishName("Solomon Islands").nativeName("Solomon Islands").build());
            countryRepository.save(Country.builder().iso2Code("SO").iso3Code("SOM").englishName("Somalia").nativeName("Somalia").build());
            countryRepository.save(Country.builder().iso2Code("ZA").iso3Code("ZAF").englishName("South Africa").nativeName("South Africa").build());
            countryRepository.save(Country.builder().iso2Code("KR").iso3Code("KOR").englishName("South Korea").nativeName("South Korea").build());
            countryRepository.save(Country.builder().iso2Code("SS").iso3Code("SSD").englishName("South Sudan").nativeName("South Sudan").build());
            countryRepository.save(Country.builder().iso2Code("ES").iso3Code("ESP").englishName("Spain").nativeName("Spain").build());
            countryRepository.save(Country.builder().iso2Code("LK").iso3Code("LKA").englishName("Sri Lanka").nativeName("Sri Lanka").build());
            countryRepository.save(Country.builder().iso2Code("SD").iso3Code("SDN").englishName("Sudan").nativeName("Sudan").build());
            countryRepository.save(Country.builder().iso2Code("SR").iso3Code("SUR").englishName("Suriname").nativeName("Suriname").build());
            countryRepository.save(Country.builder().iso2Code("SE").iso3Code("SWE").englishName("Sweden").nativeName("Sweden").build());
            countryRepository.save(Country.builder().iso2Code("CH").iso3Code("CHE").englishName("Switzerland").nativeName("Switzerland").build());
            countryRepository.save(Country.builder().iso2Code("SY").iso3Code("SYR").englishName("Syria").nativeName("Syria").build());
            countryRepository.save(Country.builder().iso2Code("TJ").iso3Code("TJK").englishName("Tajikistan").nativeName("Tajikistan").build());
            countryRepository.save(Country.builder().iso2Code("TZ").iso3Code("TZA").englishName("Tanzania").nativeName("Tanzania").build());
            countryRepository.save(Country.builder().iso2Code("TH").iso3Code("THA").englishName("Thailand").nativeName("Thailand").build());
            countryRepository.save(Country.builder().iso2Code("TL").iso3Code("TLS").englishName("Timor-Leste").nativeName("Timor-Leste").build());
            countryRepository.save(Country.builder().iso2Code("TG").iso3Code("TGO").englishName("Togo").nativeName("Togo").build());
            countryRepository.save(Country.builder().iso2Code("TO").iso3Code("TON").englishName("Tonga").nativeName("Tonga").build());
            countryRepository.save(Country.builder().iso2Code("TT").iso3Code("TTO").englishName("Trinidad and Tobago").nativeName("Trinidad and Tobago").build());
            countryRepository.save(Country.builder().iso2Code("TN").iso3Code("TUN").englishName("Tunisia").nativeName("Tunisia").build());
            countryRepository.save(Country.builder().iso2Code("TR").iso3Code("TUR").englishName("Turkey").nativeName("Turkey").build());
            countryRepository.save(Country.builder().iso2Code("TM").iso3Code("TKM").englishName("Turkmenistan").nativeName("Turkmenistan").build());
            countryRepository.save(Country.builder().iso2Code("TV").iso3Code("TUV").englishName("Tuvalu").nativeName("Tuvalu").build());
            countryRepository.save(Country.builder().iso2Code("UG").iso3Code("UGA").englishName("Uganda").nativeName("Uganda").build());
            countryRepository.save(Country.builder().iso2Code("UA").iso3Code("UKR").englishName("Ukraine").nativeName("Ukraine").build());
            countryRepository.save(Country.builder().iso2Code("AE").iso3Code("ARE").englishName("United Arab Emirates").nativeName("United Arab Emirates").build());
            countryRepository.save(Country.builder().iso2Code("GB").iso3Code("GBR").englishName("United Kingdom").nativeName("United Kingdom").build());
            countryRepository.save(Country.builder().iso2Code("US").iso3Code("USA").englishName("United States").nativeName("United States").build());
            countryRepository.save(Country.builder().iso2Code("UY").iso3Code("URY").englishName("Uruguay").nativeName("Uruguay").build());
            countryRepository.save(Country.builder().iso2Code("UZ").iso3Code("UZB").englishName("Uzbekistan").nativeName("Uzbekistan").build());
            countryRepository.save(Country.builder().iso2Code("VU").iso3Code("VUT").englishName("Vanuatu").nativeName("Vanuatu").build());
            countryRepository.save(Country.builder().iso2Code("VE").iso3Code("VEN").englishName("Venezuela").nativeName("Venezuela").build());
            countryRepository.save(Country.builder().iso2Code("VN").iso3Code("VNM").englishName("Vietnam").nativeName("Vietnam").build());
            countryRepository.save(Country.builder().iso2Code("YE").iso3Code("YEM").englishName("Yemen").nativeName("Yemen").build());
            countryRepository.save(Country.builder().iso2Code("ZM").iso3Code("ZMB").englishName("Zambia").nativeName("Zambia").build());
            countryRepository.save(Country.builder().iso2Code("ZW").iso3Code("ZWE").englishName("Zimbabwe").nativeName("Zimbabwe").build());
            countryRepository.save(Country.builder().iso2Code("HK").iso3Code("HKG").englishName("Hong Kong").nativeName("香港").build());

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
            languageRepository.save(Language.builder().iso6391("fil").iso6392B("fil").iso6392T("fil").englishTitle("Filipino").originalTitle("Filipino").build());
            languageRepository.save(Language.builder().iso6391("nb").iso6392B("nob").iso6392T("nob").englishTitle("Norwegian Bokmål").originalTitle("Bokmål").build());
            languageRepository.save(Language.builder().iso6391("bfi").iso6392B("bfi").iso6392T("bfi").englishTitle("BritishSign").originalTitle("BritishSign").build());
            languageRepository.save(Language.builder().iso6391("yue").iso6392B("yue").iso6392T("yue").englishTitle("Cantonese").originalTitle("粵語").build());
            languageRepository.save(Language.builder().iso6391("la").iso6392B("lat").iso6392T("lat").englishTitle("Latin").originalTitle("Lingua Latina").build());
            languageRepository.save(Language.builder().iso6391("ca").iso6392B("cat").iso6392T("cat").englishTitle("Catalan").originalTitle("Català").build());
            languageRepository.save(Language.builder().iso6391("ase").iso6392B("ase").iso6392T("ase").englishTitle("AmericanSign").originalTitle("AmericanSign").build());
            languageRepository.save(Language.builder().iso6391("mr").iso6392B("mar").iso6392T("mar").englishTitle("Marathi").originalTitle("मराठी").build());
            languageRepository.save(Language.builder().iso6391("aii").iso6392B("aii").iso6392T("aii").englishTitle("AssyrianNeo-Aramaic").originalTitle("ܕܸܠܵܢܵܝܵܐ ܐܵܬܘܿܪܵܝܵܐ").build());
            languageRepository.save(Language.builder().iso6391("sux").iso6392B("sux").iso6392T("sux").englishTitle("Sumerian").originalTitle("𒅴𒂠").build());
            languageRepository.save(Language.builder().iso6391("sa").iso6392B("san").iso6392T("san").englishTitle("Sanskrit").originalTitle("संस्कृतम्").build());
            languageRepository.save(Language.builder().iso6391("grc").iso6392B("grc").iso6392T("grc").englishTitle("Ancient(to1453)").originalTitle("Ἑλληνικὴ ἀρχαία").build());
            languageRepository.save(Language.builder().iso6391("Mandarin").iso6392B("Mandarin").iso6392T("Mandarin").englishTitle("Mandarin").originalTitle("普通话").build());
            languageRepository.save(Language.builder().iso6391("ga").iso6392B("gle").iso6392T("gle").englishTitle("IrishGaelic").originalTitle("Gaeilge").build());
            languageRepository.save(Language.builder().iso6391("osa").iso6392B("osa").iso6392T("osa").englishTitle("Osage").originalTitle("𐓣𐓘𐓻𐓘𐓮𐓟").build());
            languageRepository.save(Language.builder().iso6391("ins").iso6392B("ins").iso6392T("ins").englishTitle("Sign").originalTitle("International Sign").build());
            languageRepository.save(Language.builder().iso6391("vla").iso6392B("vla").iso6392T("vla").englishTitle("Flemish").originalTitle("Vlaams").build());
            languageRepository.save(Language.builder().iso6391("bg").iso6392B("bul").iso6392T("bul").englishTitle("Bulgarian").originalTitle("Български").build());
            languageRepository.save(Language.builder().iso6391("lt").iso6392B("lit").iso6392T("lit").englishTitle("Lithuanian").originalTitle("Lietuvių").build());
            languageRepository.save(Language.builder().iso6391("sw").iso6392B("swa").iso6392T("swa").englishTitle("Swahili").originalTitle("Kiswahili").build());
            languageRepository.save(Language.builder().iso6391("rom").iso6392B("rom").iso6392T("rom").englishTitle("Romany").originalTitle("Romani").build());
            languageRepository.save(Language.builder().iso6391("kn").iso6392B("kan").iso6392T("kan").englishTitle("Kannada").originalTitle("ಕನ್ನಡ").build());
            languageRepository.save(Language.builder().iso6391("aa").iso6392B("aar").iso6392T("aar").englishTitle("Afar").originalTitle("Afaraf").build());
            languageRepository.save(Language.builder().iso6391("ab").iso6392B("abk").iso6392T("abk").englishTitle("Abkhazian").originalTitle("аҧсуа бызшәа").build());
            languageRepository.save(Language.builder().iso6391("ae").iso6392B("ave").iso6392T("ave").englishTitle("Avestan").originalTitle("avesta").build());
            languageRepository.save(Language.builder().iso6391("ak").iso6392B("aka").iso6392T("aka").englishTitle("Akan").originalTitle("Akan").build());
            languageRepository.save(Language.builder().iso6391("am").iso6392B("amh").iso6392T("amh").englishTitle("Amharic").originalTitle("አማርኛ").build());
            languageRepository.save(Language.builder().iso6391("an").iso6392B("arg").iso6392T("arg").englishTitle("Aragonese").originalTitle("aragonés").build());
            languageRepository.save(Language.builder().iso6391("as").iso6392B("asm").iso6392T("asm").englishTitle("Assamese").originalTitle("অসমীয়া").build());
            languageRepository.save(Language.builder().iso6391("av").iso6392B("ava").iso6392T("ava").englishTitle("Avaric").originalTitle("авар мацӀ").build());
            languageRepository.save(Language.builder().iso6391("ay").iso6392B("aym").iso6392T("aym").englishTitle("Aymara").originalTitle("aymar aru").build());
            languageRepository.save(Language.builder().iso6391("az").iso6392B("aze").iso6392T("aze").englishTitle("Azerbaijani").originalTitle("azərbaycan dili").build());

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
                            .videoResolutions(resolutionRepository.findAll())
                            .firstVideoBitrateValueRange(0)
                            .secondVideoBitrateValueRange(200000000)
                            .firstAudioBitrateValueRange(0)
                            .secondAudioBitrateValueRange(2048000)
                            .audioChannels(audioChannelsRepository.findAll())
                            .firstVideoSizeRange(0.0)
                            .secondVideoSizeRange(31457280.0)
                            .audioCodecs(codecRepository.findByMediaType(MediaTypes.AUDIO))
                            .videoCodecs(codecRepository.findByMediaType(MediaTypes.VIDEO))
                            .genres(genreRepository.findAll())
                            .audioLanguages(languageRepository.findAll())
                    .build());
        }

        //Sources data loader
        if(sourcePathRepository.count() == 0){
            sourcePathRepository.save(SourcePath.builder()
                            .libraryItem(LibraryItems.MOVIE)
                            .path("Z:\\Downloads\\Movies")
                            .title("Movies Download Path")
                            .pathType(SourcePath.PathType.DOWNLOAD)
                    .build());

            sourcePathRepository.save(SourcePath.builder()
                    .libraryItem(LibraryItems.MOVIE)
                    .path("Z:\\MultiMedia\\Movies")
                    .title("Movies Main Path")
                    .pathType(SourcePath.PathType.SOURCE)
                    .build());
        }
    }
}
