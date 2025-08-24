package org.sda.mediaporter.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.Services.Impl.AudioServiceImpl;
import org.sda.mediaporter.models.Language;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.AudioChannel;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.LanguageRepository;
import org.sda.mediaporter.repositories.MovieRepository;
import org.sda.mediaporter.repositories.metadata.AudioChannelRepository;
import org.sda.mediaporter.repositories.metadata.AudioRepository;
import org.sda.mediaporter.repositories.metadata.CodecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class AudioServiceTest {

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AudioChannelRepository audioChannelsRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AudioService audioService;

    @Autowired
    private AudioServiceImpl audioServiceImpl;


    @BeforeEach
    void setup(){
        movieRepository.deleteAll();
        configurationRepository.deleteAll();
        codecRepository.deleteAll();
        languageRepository.deleteAll();
        audioChannelsRepository.deleteAll();
        audioRepository.deleteAll();

        Codec aac3Codec = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("EAC3")
                .build());

        Codec aacCodec = codecRepository.save(Codec.builder()
                .mediaType(MediaTypes.AUDIO)
                .name("AAC")
                .build());

        Language englishLanguage = languageRepository.save(Language.builder()
                .iso6391("en")
                .iso6392B("eng")
                .iso6392T("eng")
                .englishTitle("English")
                .originalTitle("English").build());

        Language spanishLanguage = languageRepository.save(Language.builder()
                .iso6391("es")
                .iso6392B("spa")
                .iso6392T("spa")
                .englishTitle("Spanish")
                .originalTitle("Español").build());

        AudioChannel stereoAudioChannel = audioChannelsRepository.save(AudioChannel.builder()
                .title("2 Stereo")
                .channels(2)
                .description("Two-channel stereo sound")
                .build());

        AudioChannel surroundAudioChannel = audioChannelsRepository.save(AudioChannel.builder()
                .title("5.1 Surround")
                .channels(6)
                .description("Six-channel surround (5 speakers + 1 sub)")
                .build());
    }

    @Test
    void getAudioListFromFile() {
    }

    @Test
    void getAudioCodec_successfully() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000","eng"};

        //Act
        Codec audioCodec = audioService.getAudioCodec(audioProperties[0]);

        //Assert
        assertNotNull(audioCodec.getId());
        assertEquals("AAC", audioCodec.getName());
        assertEquals(MediaTypes.AUDIO, audioCodec.getMediaType());
    }

    @Test
    void getAudioCodec_successfullyUnknownCodec() {
        //Arrest
        String[] audioProperties = new String[]{"mp3", "2","128000","eng"};

        //Act
        Codec audioCodec = audioService.getAudioCodec(audioProperties[0]);

        //Assert
        assertNotNull(audioCodec.getId());
        assertEquals("mp3", audioCodec.getName());
        assertEquals(MediaTypes.AUDIO, audioCodec.getMediaType());
    }

    @Test
    void getAudioCodec_successfullyWithSpecialChar() {
        //Arrest
        String[] audioProperties = new String[]{"a-a/ c", "2","128000","eng"};

        //Act
        Codec audioCodec = audioService.getAudioCodec(audioProperties[0]);

        //Assert
        assertNotNull(audioCodec.getId());
        assertEquals("AAC", audioCodec.getName());
        assertEquals(MediaTypes.AUDIO, audioCodec.getMediaType());
    }

    @Test
    void getAudioCodec_withNA() {
        //Arrest
        String[] audioProperties = new String[]{"N/A", "2","128000","eng"};

        //Act
        Codec audioCodec = audioService.getAudioCodec(audioProperties[0]);

        //Assert
        assertNull(audioCodec);
    }

    @Test
    void getAudioCodec_withBlankCodec() {
        //Arrest
        String[] audioProperties = new String[]{"", "2","128000","eng"};

        //Act
        Codec audioCodec = audioService.getAudioCodec(audioProperties[0]);

        //Assert
        assertNull(audioCodec);
    }

    @Test
    void getAudioCodec_withNullCodec() {
        //Arrest
        String[] audioProperties = new String[]{null, "2","128000","eng"};

        //Act
        Codec audioCodec = audioService.getAudioCodec(audioProperties[0]);

        //Assert
        assertNull(audioCodec);
    }

    @Test
    void getAudioChannel_successfully() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000","eng"};

        //Act
        AudioChannel audioChannel = audioService.getAudioChannel(audioProperties[1]);

        //Assert
        assertNotNull(audioChannel.getId());
        assertEquals("2 Stereo", audioChannel.getTitle());
        assertEquals(2, audioChannel.getChannels());
        assertEquals("Two-channel stereo sound", audioChannel.getDescription());
    }

    @Test
    void getAudioChannel_unknownChannels() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "5","128000","eng"};

        //Act
        AudioChannel audioChannel = audioService.getAudioChannel(audioProperties[1]);
        //Assert
        assertNotNull(audioChannel.getId());
        assertEquals(5, audioChannel.getChannels());
        assertEquals("5 Channels Sound", audioChannel.getTitle());

    }

    @Test
    void getAudioChannel_successfullyWithSpecialChar() {
        //Arrest
        String[] audioProperties = new String[]{"aac", " 2-","128000","eng"};

        //Act
        AudioChannel audioChannel = audioService.getAudioChannel(audioProperties[1]);

        //Assert
        assertNotNull(audioChannel.getId());
        assertEquals("2 Stereo", audioChannel.getTitle());
        assertEquals(2, audioChannel.getChannels());
        assertEquals("Two-channel stereo sound", audioChannel.getDescription());
    }

    @Test
    void getAudioChannel_withNA() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "N/A","128000","eng"};

        //Act
        AudioChannel audioChannel = audioService.getAudioChannel(audioProperties[1]);

        //Assert
        assertNull(audioChannel);
    }

    @Test
    void getAudioChannel_withBlankChannels() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "","128000","eng"};

        //Act
        AudioChannel audioChannel = audioService.getAudioChannel(audioProperties[1]);

        //Assert
        assertNull(audioChannel);
    }

    @Test
    void getAudioChannel_withNullChannel() {
        //Arrest
        String[] audioProperties = new String[]{"aac", null,"128000","eng"};

        //Act
        AudioChannel audioChannel = audioService.getAudioChannel(audioProperties[1]);

        //Assert
        assertNull(audioChannel);
    }

    @Test
    void getAudioBitrate() {
    }

    @Test
    void getAudioBitrate_successfully() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000","eng"};

        //Act
        Integer bitrate = audioService.getAudioBitrate(audioProperties[2]);

        //Assert
        assertEquals(128, bitrate);
    }

    @Test
    void getAudioBitrate_successfullyWithSpecialChar() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2"," 128-00 0 ","eng"};

        //Act
        Integer bitrate = audioService.getAudioBitrate(audioProperties[2]);

        //Assert
        assertEquals(128, bitrate);
    }

    @Test
    void getAudioBitrate_withNA() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","N/A","eng"};

        //Act
        Integer bitrate = audioService.getAudioBitrate(audioProperties[2]);

        //Assert
        assertNull(bitrate);
    }

    @Test
    void getAudioBitrate_withBlankBitrate() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","","eng"};

        //Act
        Integer bitrate = audioService.getAudioBitrate(audioProperties[2]);

        //Assert
        assertNull(bitrate);
    }

    @Test
    void getAudioBitrate_withNullBitrate() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2",null,"eng"};

        //Act
        Integer bitrate = audioService.getAudioBitrate(audioProperties[2]);

        //Assert
        assertNull(bitrate);
    }


    @Test
    void getAudioLanguageByCode_successfullyWithIso6392B() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000","spa"};

        //Act
        Language language = audioService.getAudioLanguageByCode(audioProperties[3]);

        //Assert
        assertNotNull(language.getId());
        assertEquals("es", language.getIso6391());
        assertEquals("spa", language.getIso6392B());
        assertEquals("spa", language.getIso6392T());
        assertEquals("Spanish", language.getEnglishTitle());
        assertEquals("Español", language.getOriginalTitle());
    }

    @Test
    void getAudioLanguageByCode_successfullyWithIso6391() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000","es"};

        //Act
        Language language = audioService.getAudioLanguageByCode(audioProperties[3]);

        //Assert
        assertNotNull(language.getId());
        assertEquals("es", language.getIso6391());
        assertEquals("spa", language.getIso6392B());
        assertEquals("spa", language.getIso6392T());
        assertEquals("Spanish", language.getEnglishTitle());
        assertEquals("Español", language.getOriginalTitle());
    }

    @Test
    void getAudioLanguageByCode_successfullyWithEnglishTitle() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000","Spanish"};

        //Act
        Language language = audioService.getAudioLanguageByCode(audioProperties[3]);

        //Assert
        assertNotNull(language.getId());
        assertEquals("es", language.getIso6391());
        assertEquals("spa", language.getIso6392B());
        assertEquals("spa", language.getIso6392T());
        assertEquals("Spanish", language.getEnglishTitle());
        assertEquals("Español", language.getOriginalTitle());
    }

    @Test
    void getAudioLanguageByCode_successfullyWithOriginalTitle() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000","Español"};

        //Act
        Language language = audioService.getAudioLanguageByCode(audioProperties[3]);

        //Assert
        assertNotNull(language.getId());
        assertEquals("es", language.getIso6391());
        assertEquals("spa", language.getIso6392B());
        assertEquals("spa", language.getIso6392T());
        assertEquals("Spanish", language.getEnglishTitle());
        assertEquals("Español", language.getOriginalTitle());
    }

    @Test
    void getAudioLanguageByCode_successfullyUnknownLanguage() {
        //Arrest
        String[] audioProperties = new String[]{"mp3", "2","128000","FI"};

        //Act
        Language language = audioService.getAudioLanguageByCode(audioProperties[3]);

        //Assert
        assertNull(language);
    }

    @Test
    void getAudioLanguageByCode_withNA() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000","N/A"};

        //Act
        Language language = audioService.getAudioLanguageByCode(audioProperties[3]);

        //Assert
        assertNull(language);
    }

    @Test
    void getAudioLanguageByCode_withBlankLanguage() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000",""};

        //Act
        Language language = audioService.getAudioLanguageByCode(audioProperties[3]);

        //Assert
        assertNull(language);
    }

    @Test
    void getAudioLanguageByCode_withNullLanguage() {
        //Arrest
        String[] audioProperties = new String[]{"aac", "2","128000",null};

        //Act
        Language language = audioService.getAudioLanguageByCode(audioProperties[3]);

        //Assert
        assertNull(language);
    }

    @Test
    void testArrayLength_allParameters(){
        //Arrest
        String audioProperties = "aac,2,128000,eng";

        //Act
        String[] properties = audioServiceImpl.properties(audioProperties);

        //Assert
        assertEquals(4, properties.length);
    }

    @Test
    void testArrayLength_withLastBlankParam(){
        //Arrest
        String audioProperties = "aac,2,128000,";

        //Act
        String[] properties = audioServiceImpl.properties(audioProperties);

        //Assert
        assertEquals(4, properties.length);
    }
}