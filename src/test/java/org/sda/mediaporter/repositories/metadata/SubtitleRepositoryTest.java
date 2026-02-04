package org.sda.mediaporter.repositories.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.metadata.Subtitle;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class SubtitleRepositoryTest {

    @Autowired
    private SubtitleRepository subtitleRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private Subtitle subtitle;

    @BeforeEach
    void loadData(){
        this.subtitle = testDataFactory.createTvShowSubtitle(null);
    }

    @Test
    void findById(){
        //Arrest
        Long subtitleId = this.subtitle.getId();
        //Act
        Optional<Subtitle> result = subtitleRepository.findById(subtitleId);
        //Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
    }
}