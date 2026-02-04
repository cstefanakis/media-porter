package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.testutil.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestDataFactory.class)
class SourcePathRepositoryTest {

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void loadData(){
        testDataFactory.createSourcePathMovieDownloads();
    }

    @Test
    void findByPath_true() {
        //Arrest
        String path = Path.of("src\\test\\resources\\downloadSources\\movies").normalize().toString();
        //Act
        Optional<SourcePath> result = sourcePathRepository.findByPath(path);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByPath_notNormalizePath() {
        //Arrest
        String path = "src/test/resources/downloadSources/movies";
        //Act
        Optional<SourcePath> result = sourcePathRepository.findByPath(path);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByTitle_true() {
        //Arrest
        String title = "Movies download source";
        //Act
        Optional<SourcePath> result = sourcePathRepository.findByTitle(title);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findByTitle_false() {
        //Arrest
        String title = "no source";
        //Act
        Optional<SourcePath> result = sourcePathRepository.findByTitle(title);
        //Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findSourcePathsByPathType() {
        //Arrest
        SourcePath.PathType download = SourcePath.PathType.DOWNLOAD;
        //Act
        List<SourcePath> result= sourcePathRepository.findSourcePathsByPathType(download);
        //Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findSourcePathByPathTypeAndLibraryItem() {
        //Arrest
        SourcePath.PathType download = SourcePath.PathType.DOWNLOAD;
        LibraryItems libraryItem = LibraryItems.MOVIE;
        //Act
        List<SourcePath> result = sourcePathRepository.findSourcePathByPathTypeAndLibraryItem(download, libraryItem);
        //Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findRootPathOfFile() {
        //Arrest
        String filePath = Path.of("src/test/resources/downloadSources/movies/file.mp4").normalize().toString();
        String rootPath = Path.of("src/test/resources/downloadSources/movies").normalize().toString();
        //Act
        Optional<String> result = sourcePathRepository.findRootPathOfFile(filePath);
        //Assert
        assertTrue(result.isPresent());
        assertEquals(rootPath, result.get());
    }

    @Test
    void findSourcePathByPath() {
        //Arrest
        String rootPath = Path.of("src/test/resources/downloadSources/movies").normalize().toString();
        //Act
        Optional<SourcePath> result = sourcePathRepository.findSourcePathByPath(rootPath);
        //Assert
        assertTrue(result.isPresent());
    }

    @Test
    void findSourcePathsByLibraryItem() {
        //Arrest
        LibraryItems libraryItem = LibraryItems.MOVIE;
        //Act
        List<SourcePath> result = sourcePathRepository.findSourcePathsByLibraryItem(libraryItem);
        //Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());

    }
}