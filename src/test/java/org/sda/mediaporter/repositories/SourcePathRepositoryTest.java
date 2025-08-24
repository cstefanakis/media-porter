package org.sda.mediaporter.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.SourcePath.PathType;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.xml.transform.Source;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("Test")
class SourcePathRepositoryTest {

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @BeforeEach
    void setup(){
        sourcePathRepository.deleteAll();
        sourcePathRepository.save(SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .title("Downloads")
                .pathType(PathType.DOWNLOAD)
                .path("src/test/resources/movies/downloads")
                .build());

        sourcePathRepository.save(SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .title("External")
                .pathType(PathType.EXTERNAL)
                .path("src/test/resources/movies/external")
                .build());

        sourcePathRepository.save(SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .title("Movies")
                .pathType(PathType.SOURCE)
                .path("src/test/resources/movies/movies")
                .build());
    }

    @Test
    void findByPath() {
        //Arrest
        String downloadPath = "src/test/resources/movies/downloads";
        String externalPath = "src/test/resources/movies/external";
        String moviePath = "src/test/resources/movies/movies";
        String noPath = "";

        //Act
        SourcePath downloadSource = sourcePathRepository.findByPath(downloadPath).orElse(null);
        SourcePath externalSource = sourcePathRepository.findByPath(externalPath).orElse(null);
        SourcePath movieSource = sourcePathRepository.findByPath(moviePath).orElse(null);
        SourcePath noFound = sourcePathRepository.findByPath(noPath).orElse(null);

        //Assert
        assertNotNull(externalSource);
        assertEquals("External", externalSource.getTitle());
        assertEquals(LibraryItems.MOVIE, externalSource.getLibraryItem());
        assertEquals(PathType.EXTERNAL, externalSource.getPathType());
        assertEquals("src/test/resources/movies/external", externalSource.getPath());

        assertNotNull(downloadSource);
        assertEquals("Downloads", downloadSource.getTitle());
        assertEquals(LibraryItems.MOVIE, downloadSource.getLibraryItem());
        assertEquals(PathType.DOWNLOAD, downloadSource.getPathType());
        assertEquals("src/test/resources/movies/downloads", downloadSource.getPath());

        assertNotNull(movieSource);
        assertEquals("Movies", movieSource.getTitle());
        assertEquals(LibraryItems.MOVIE, movieSource.getLibraryItem());
        assertEquals(PathType.SOURCE, movieSource.getPathType());
        assertEquals("src/test/resources/movies/movies", movieSource.getPath());

        assertNull(noFound);
    }

    @Test
    void findByTitle() {
        //Arrest
        String downloadSourcePathTitle = "Downloads";
        String externalSourcePathTitle = "External";
        String moviesSourcePathTitle = "Movies";
        String noTitleFound = "";

        //Act
        SourcePath downloadSource = sourcePathRepository.findByTitle(downloadSourcePathTitle).orElse(null);
        SourcePath externalSource = sourcePathRepository.findByTitle(externalSourcePathTitle).orElse(null);
        SourcePath movieSource = sourcePathRepository.findByTitle(moviesSourcePathTitle).orElse(null);
        SourcePath noFound = sourcePathRepository.findByTitle(noTitleFound).orElse(null);

        //Assert
        assertNotNull(externalSource);
        assertEquals("External", externalSource.getTitle());
        assertEquals(LibraryItems.MOVIE, externalSource.getLibraryItem());
        assertEquals(PathType.EXTERNAL, externalSource.getPathType());
        assertEquals("src/test/resources/movies/external", externalSource.getPath());

        assertNotNull(downloadSource);
        assertEquals("Downloads", downloadSource.getTitle());
        assertEquals(LibraryItems.MOVIE, downloadSource.getLibraryItem());
        assertEquals(PathType.DOWNLOAD, downloadSource.getPathType());
        assertEquals("src/test/resources/movies/downloads", downloadSource.getPath());

        assertNotNull(movieSource);
        assertEquals("Movies", movieSource.getTitle());
        assertEquals(LibraryItems.MOVIE, movieSource.getLibraryItem());
        assertEquals(PathType.SOURCE, movieSource.getPathType());
        assertEquals("src/test/resources/movies/movies", movieSource.getPath());

        assertNull(noFound);
    }

    @Test
    void findSourcePathsByPathType() {
        //Arrest
        SourcePath.PathType downloadSourcePathPathType = PathType.DOWNLOAD;
        SourcePath.PathType externalSourcePathPathType = PathType.EXTERNAL;
        SourcePath.PathType moviesSourcePathPathType = PathType.SOURCE;

        //Act
        List<SourcePath> downloadSource = sourcePathRepository.findSourcePathsByPathType(downloadSourcePathPathType);
        List<SourcePath> externalSource = sourcePathRepository.findSourcePathsByPathType(externalSourcePathPathType);
        List<SourcePath> movieSource = sourcePathRepository.findSourcePathsByPathType(moviesSourcePathPathType);

        //Assert
        assertEquals(1, downloadSource.size());
        assertEquals(1, externalSource.size());
        assertEquals(1, movieSource.size());
    }

    @Test
    void findSourcePathByPathTypeAndLibraryItem() {
        //Arrest
        SourcePath.PathType downloadSourcePathPathType = PathType.DOWNLOAD;
        SourcePath.PathType externalSourcePathPathType = PathType.EXTERNAL;
        SourcePath.PathType moviesSourcePathPathType = PathType.SOURCE;

        LibraryItems movies = LibraryItems.MOVIE;

        //Act
        List<SourcePath> downloadSource = sourcePathRepository.findSourcePathByPathTypeAndLibraryItem(downloadSourcePathPathType, movies);
        List<SourcePath> externalSource = sourcePathRepository.findSourcePathByPathTypeAndLibraryItem(externalSourcePathPathType, movies);
        List<SourcePath> movieSource = sourcePathRepository.findSourcePathByPathTypeAndLibraryItem(moviesSourcePathPathType, movies);

        //Assert
        assertEquals(1, downloadSource.size());
        assertEquals(1, externalSource.size());
        assertEquals(1, movieSource.size());
    }
}