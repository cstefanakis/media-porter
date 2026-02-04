package org.sda.mediaporter.repositories.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.enums.MediaTypes;
import org.sda.mediaporter.models.metadata.Codec;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class CodecRepositoryTest {

    @Autowired
    private CodecRepository codecRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Codec h264;
    private Codec h265;

    @BeforeEach
    void loadData(){
        this.h264 = testDataFactory.createCodecH264();
        this.h265 = testDataFactory.createCodecH265();
    }

    @Test
    void findByName_true() {
        //Arrest
        String name = this.h264.getName();
        //Act
        Optional<Codec> result = codecRepository.findByName(name);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByName_false() {
        //Arrest
        String name = "noName";
        //Act
        Optional<Codec> result = codecRepository.findByName(name);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByName_null() {
        //Act
        Optional<Codec> result = codecRepository.findByName(null);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByNameAndMediaType() {
        //Arrest
        MediaTypes videoMediaType = MediaTypes.VIDEO;
        String name = this.h265.getName();
        //Act
        Optional<Codec> result = codecRepository.findByNameAndMediaType(name, videoMediaType);
        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
    }

    @Test
    void findByMediaType() {
        //Arrest
        MediaTypes videoMediaType = MediaTypes.VIDEO;
        //Act
        List<Codec> result = codecRepository.findByMediaType(videoMediaType);
        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findIdsByMediaType() {
        //Arrest
        Long h264Id = this.h264.getId();
        MediaTypes videoMediaType = MediaTypes.VIDEO;
        //Act
        List<Long> result = codecRepository.findIdsByMediaType(videoMediaType);
        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(h264Id));
    }
}