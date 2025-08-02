package org.sda.mediaporter.Services.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.Services.SourcePathService;
import org.sda.mediaporter.dtos.SourcePathDto;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SourcePathServiceImplTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private SourcePathService sourcePathService;

    private SourcePath movieSourcePath;
    private SourcePath movieDownloadSourcePath;

    @BeforeEach
    void setup(){
        configurationRepository.deleteAll();
        sourcePathRepository.deleteAll();

        movieDownloadSourcePath = sourcePathRepository.save(SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\Downloads\\Movies")
                .title("Movies Download Path")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .build());

        movieSourcePath = sourcePathRepository.save(SourcePath.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\MultiMedia\\Movies")
                .title("Movies Main Path")
                .pathType(SourcePath.PathType.SOURCE)
                .build());
    }

    @Test
    void getById_successfully() {
        //Arrest
        Long movieSourceId = movieSourcePath.getId();

        //Act
        SourcePath movieSourcePath = sourcePathService.getById(movieSourceId);

        //Assert
        assertNotNull(movieSourcePath.getId());
        assertEquals(LibraryItems.MOVIE, movieSourcePath.getLibraryItem());
        assertEquals("Z:\\MultiMedia\\Movies", movieSourcePath.getPath());
        assertEquals("Movies Main Path", movieSourcePath.getTitle());
        assertEquals(SourcePath.PathType.SOURCE, movieSourcePath.getPathType());
    }

    @Test
    void getById_notExistId() {
        //Arrest
        Long notExistId = 0L;

        //Assert and Act
        assertThrows(EntityNotFoundException.class, () -> sourcePathService.getById(notExistId));
    }

    @Test
    void getSourcePaths_successfully() {
        //Act
        List <SourcePath> sourcePaths = sourcePathService.getSourcePaths();

        //Assert
        assertEquals(2, sourcePaths.size());
    }

    @Test
    void deleteById_successfully() {
        //Arrest
        Long downloadMovieId = movieDownloadSourcePath.getId();

        //Act
        sourcePathService.deleteById(downloadMovieId);

        //Assert
        assertThrows(EntityNotFoundException.class, () -> sourcePathService.getById(downloadMovieId));
    }

    @Test
    void deleteById_withNotExistId() {
        //Arrest
        Long noExistId = 0L;

        //Assert
        assertThrows(EntityNotFoundException.class, () -> sourcePathService.deleteById(noExistId));
    }

    @Test
    void createSourcePath() {
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\External\\Movies")
                .title("Movies External Path")
                .pathType(SourcePath.PathType.EXTERNAL)
                .build();
    }

    @Test
    void updateSourcePath() {
    }
}