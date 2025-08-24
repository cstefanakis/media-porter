package org.sda.mediaporter.Services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class SourcePathServiceTest {

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
    void createSourcePath_withSamePath(){
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\Downloads\\Movies")
                .title("Movies External Path")
                .pathType(SourcePath.PathType.EXTERNAL)
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> sourcePathService.createSourcePath(externalPathDto));
    }

    @Test
    void createSourcePath_withSameTitle(){
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\External\\Movies")
                .title("Movies Download Path")
                .pathType(SourcePath.PathType.EXTERNAL)
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> sourcePathService.createSourcePath(externalPathDto));
    }

    @Test
    void createSourcePath_moreMovieSources(){
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\second_source\\Movies")
                .title("Movies second source")
                .pathType(SourcePath.PathType.SOURCE)
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, () -> sourcePathService.createSourcePath(externalPathDto));
    }

    @Test
    void createSourcePath_withNullPath(){
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .title("Movies External Path")
                .pathType(SourcePath.PathType.EXTERNAL)
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> sourcePathService.createSourcePath(externalPathDto));
    }

    @Test
    void createSourcePath_withNullLibraryItem(){
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .path("Z:\\External\\Movies")
                .title("Movies External Path")
                .pathType(SourcePath.PathType.EXTERNAL)
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> sourcePathService.createSourcePath(externalPathDto));
    }

    @Test
    void createSourcePath_withNullTitle(){
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\External\\Movies")
                .pathType(SourcePath.PathType.EXTERNAL)
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> sourcePathService.createSourcePath(externalPathDto));
    }

    @Test
    void createSourcePath_withNullPathType(){
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\External\\Movies")
                .title("Movies External Path")
                .build();

        //Assert and Act
        assertThrows(RuntimeException.class, () -> sourcePathService.createSourcePath(externalPathDto));
    }

    @Test
    void createSourcePath_withExistSameLibraryItemsAndPathType() {
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\External\\Movies")
                .title("Movies External Path")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .build();

        //Act and Assert
        assertThrows(EntityExistsException.class, () -> sourcePathService.createSourcePath(externalPathDto));
    }

    @Test
    void createSourcePath_successfully() {
        //Arrest
        SourcePathDto externalPathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\External\\Movies")
                .title("Movies External Path")
                .pathType(SourcePath.PathType.EXTERNAL)
                .build();

        //Act
        SourcePath createdSourcePath = sourcePathService.createSourcePath(externalPathDto);

        //Assert
        assertNotNull(createdSourcePath.getId());
        assertEquals(LibraryItems.MOVIE, createdSourcePath.getLibraryItem());
        assertEquals("Z:\\External\\Movies", createdSourcePath.getPath());
        assertEquals("Movies External Path", createdSourcePath.getTitle());
        assertEquals(SourcePath.PathType.EXTERNAL, createdSourcePath.getPathType());
    }

    @Test
    void updateSourcePath_withExistPath() {
        //Assert
        Long downloadMoviePathId = movieDownloadSourcePath.getId();
        SourcePathDto updateDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.TV_SHOW)
                .path("Z:\\MultiMedia\\Movies")
                .title("TV Shows Download Path")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, ()-> sourcePathService.updateSourcePath(downloadMoviePathId, updateDto));
    }

    @Test
    void updateSourcePath_withExistTitle() {
        //Assert
        Long downloadMoviePathId = movieDownloadSourcePath.getId();
        SourcePathDto updateDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.TV_SHOW)
                .path("Z:\\downloads\\tv_shows")
                .title("Movies Main Path")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .build();

        //Assert and Act
        assertThrows(EntityExistsException.class, ()-> sourcePathService.updateSourcePath(downloadMoviePathId, updateDto));
    }

    @Test
    void updateSourcePath_successfullyWithAllNull() {
        //Assert
        Long downloadMoviePathId = movieDownloadSourcePath.getId();
        SourcePathDto nullSourcePathDto = SourcePathDto.builder().build();

        //Act
        sourcePathService.updateSourcePath(downloadMoviePathId, nullSourcePathDto);
        SourcePath updatedSourcePath = sourcePathService.getById(downloadMoviePathId);

        //Assert
        assertEquals("Z:\\Downloads\\Movies", updatedSourcePath.getPath());
        assertEquals(LibraryItems.MOVIE, updatedSourcePath.getLibraryItem());
        assertEquals("Movies Download Path", updatedSourcePath.getTitle());
        assertEquals(SourcePath.PathType.DOWNLOAD, updatedSourcePath.getPathType());
    }

    @Test
    void updateSourcePath_successfullyWithUpdateParameters() {
        //Assert
        Long downloadMoviePathId = movieDownloadSourcePath.getId();
        SourcePathDto nullSourcePathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.MOVIE)
                .path("Z:\\Downloads\\Movies")
                .title("Movies Download Path")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .build();

        //Act
        sourcePathService.updateSourcePath(downloadMoviePathId, nullSourcePathDto);
        SourcePath updatedSourcePath = sourcePathService.getById(downloadMoviePathId);

        //Assert
        assertEquals("Z:\\Downloads\\Movies", updatedSourcePath.getPath());
        assertEquals(LibraryItems.MOVIE, updatedSourcePath.getLibraryItem());
        assertEquals("Movies Download Path", updatedSourcePath.getTitle());
        assertEquals(SourcePath.PathType.DOWNLOAD, updatedSourcePath.getPathType());
    }

    @Test
    void updateSourcePath_successfully() {
        //Assert
        Long downloadMoviePathId = movieDownloadSourcePath.getId();
        SourcePathDto nullSourcePathDto = SourcePathDto.builder()
                .libraryItem(LibraryItems.TV_SHOW)
                .path("Z:\\downloads\\tv_shows")
                .title("TV Shows Download Path")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .build();

        //Act
        sourcePathService.updateSourcePath(downloadMoviePathId, nullSourcePathDto);
        SourcePath updatedSourcePath = sourcePathService.getById(downloadMoviePathId);

        //Assert
        assertEquals("Z:\\downloads\\tv_shows", updatedSourcePath.getPath());
        assertEquals(LibraryItems.TV_SHOW, updatedSourcePath.getLibraryItem());
        assertEquals("TV Shows Download Path", updatedSourcePath.getTitle());
        assertEquals(SourcePath.PathType.DOWNLOAD, updatedSourcePath.getPathType());
    }
}