package org.sda.mediaporter.services.fileServices;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.dtos.SourcePathDto;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.sda.mediaporter.services.fileServices.impl.SourcePathServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SourcePathServiceTest {

    @Mock
    private SourcePathRepository sourcePathRepository;

    @InjectMocks
    private SourcePathServiceImpl sourcePathService;

    @TempDir
    Path tvShowDownloads;

    @TempDir
    Path tvShows;

    private SourcePath tvShowDownlaodSourcePath;
    private SourcePath tvShowSourcePath;

    @BeforeEach
    void loadData(){
        this.tvShowDownlaodSourcePath = SourcePath.builder()
                .path(this.tvShowDownloads.normalize().toString())
                .title("tv shows downloads")
                .pathType(SourcePath.PathType.DOWNLOAD)
                .libraryItem(LibraryItems.TV_SHOW)
                .build();

        this.tvShowSourcePath = SourcePath.builder()
                .path(this.tvShows.normalize().toString())
                .title("tv shows")
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.TV_SHOW)
                .build();
    }

    @Test
    void getById_exist() {
        //Arrest
        Long id = 1L;
        when(sourcePathRepository.findById(id)).thenReturn(Optional.of(this.tvShowDownlaodSourcePath));
        //Act
        SourcePath result = sourcePathService.getById(id);
        //Assert
        assertNotNull(result);
        assertEquals(this.tvShowDownlaodSourcePath.getPath(), result.getPath());
        verify(sourcePathRepository).findById(id);
    }

    @Test
    void getById_notExist() {
        //Arrest
        Long id = 1L;
        when(sourcePathRepository.findById(id)).thenReturn(Optional.empty());
        //Act
        assertThrows(EntityNotFoundException.class,
                () -> sourcePathService.getById(id));
        //Assert
        verify(sourcePathRepository).findById(id);
    }

    @Test
    void getSourcePaths() {
        //Arrest
        List<SourcePath> sourcePaths = List.of(this.tvShowDownlaodSourcePath, this.tvShowSourcePath);
        when(sourcePathRepository.findAll()).thenReturn(sourcePaths);
        //Act
        List<SourcePath> result = sourcePathService.getSourcePaths();
        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sourcePathRepository).findAll();
    }

    @Test
    void getSourcePathsByPathTypeAndLibraryItem() {
        //Arrest
        SourcePath.PathType download = SourcePath.PathType.DOWNLOAD;
        LibraryItems tvShow = LibraryItems.TV_SHOW;
        when(sourcePathRepository.findSourcePathByPathTypeAndLibraryItem(download, tvShow)).thenReturn(List.of(this.tvShowDownlaodSourcePath));
        //Act
        List<SourcePath> result = sourcePathService.getSourcePathsByPathTypeAndLibraryItem(download, tvShow);
        //Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sourcePathRepository).findSourcePathByPathTypeAndLibraryItem(download, tvShow);
    }

    @Test
    void deleteById_exist() {
        //Arrest
        Long id = 1L;
        when(sourcePathRepository.findById(id)).thenReturn(Optional.of(this.tvShowDownlaodSourcePath));
        //Act
        sourcePathService.deleteById(id);
        //Assert
        verify(sourcePathRepository).delete(this.tvShowDownlaodSourcePath);
    }

    @Test
    void deleteById_notExist() {
        //Arrest
        Long id = 1L;
        when(sourcePathRepository.findById(id)).thenReturn(Optional.empty());
        //Act

        //Assert
        assertThrows(EntityNotFoundException.class,
                () -> sourcePathService.deleteById(id));
        verify(sourcePathRepository, never()).delete(any());
    }

    @Test
    void getRootFileFromFile_exist() throws IOException {
        //Arrest
        Path filePath = this.tvShowDownloads.resolve("filePath.mp4").normalize();
        Files.createFile(filePath);
        String filePathToString = filePath.toString();
        when(sourcePathRepository.findRootPathOfFile(filePathToString)).thenReturn(Optional.of(this.tvShowDownlaodSourcePath.getPath()));
        //Act
        String result = sourcePathService.getRootFileFromFile(filePathToString);
        //Assert
        assertNotNull(result);
        verify(sourcePathRepository).findRootPathOfFile(filePathToString);
    }

    @Test
    void getRootFileFromFile_notExist() throws IOException {
        //Arrest
        Path filePath = this.tvShowDownloads.resolve("filePath.mp4").normalize();
        Files.createFile(filePath);
        String filePathToString = filePath.toString();
        when(sourcePathRepository.findRootPathOfFile(filePathToString)).thenReturn(Optional.empty());
        //Act

        //Assert
        assertThrows(EntityNotFoundException.class,
                () -> sourcePathService.getRootFileFromFile(filePathToString));
        verify(sourcePathRepository).findRootPathOfFile(filePathToString);
    }

    @Test
    void createSourcePath() {
        //Arrest
        SourcePathDto sourcePathDto = SourcePathDto.builder()
                .path(this.tvShowDownlaodSourcePath.getPath())
                .pathType(this.tvShowDownlaodSourcePath.getPathType())
                .title(this.tvShowDownlaodSourcePath.getTitle())
                .libraryItem(this.tvShowDownlaodSourcePath.getLibraryItem())
                .build();
        when(sourcePathRepository.save(any(SourcePath.class)))
                .thenReturn(this.tvShowDownlaodSourcePath);
        //Act
        SourcePath result = sourcePathService.createSourcePath(sourcePathDto);
        //Assert
        assertNotNull(result);
        assertEquals(this.tvShowDownlaodSourcePath, result);
        verify(sourcePathRepository).save(any(SourcePath.class));
    }

    @Test
    void updateSourcePath() {
        //Arrest
        Long id = 1L;
        String updatedTitle = "updatedTitle";
        SourcePathDto sourcePathDto = SourcePathDto.builder()
                .path(this.tvShowDownlaodSourcePath.getPath())
                .pathType(this.tvShowDownlaodSourcePath.getPathType())
                .title(updatedTitle)
                .libraryItem(this.tvShowDownlaodSourcePath.getLibraryItem())
                .build();
        when(sourcePathRepository.findById(id)).thenReturn(Optional.of(this.tvShowDownlaodSourcePath));
        when(sourcePathRepository.save(any(SourcePath.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //Act
        SourcePath result = sourcePathService.updateSourcePath(id, sourcePathDto);
        //Assert
        assertNotNull(result);
        assertEquals(updatedTitle, result.getTitle());
        verify(sourcePathRepository).findById(id);
        verify(sourcePathRepository).save(any(SourcePath.class));
    }

    @Test
    void getSourcePathFromPath() throws IOException {
        //Arrest
        Path filePath = this.tvShowDownloads.resolve("filePath.mp4").normalize();
        Files.createFile(filePath);
        String filePathToString = filePath.toString();
        when(sourcePathRepository.findSourcePathByPath(filePathToString)).thenReturn(Optional.of(this.tvShowDownlaodSourcePath));
        //Act
        SourcePath result = sourcePathService.getSourcePathFromPath(filePath);
        //Assert
        assertNotNull(result);
        verify(sourcePathRepository).findSourcePathByPath(filePathToString);
    }

    @Test
    void getSourcePathsByLibraryItem() {
        //Arrest
        LibraryItems tvShow = LibraryItems.TV_SHOW;
        when(sourcePathRepository.findSourcePathsByLibraryItem(tvShow))
                .thenReturn(List.of(this.tvShowDownlaodSourcePath, this.tvShowSourcePath));
        //Act
        List<SourcePath> result = sourcePathService.getSourcePathsByLibraryItem(tvShow);
        //Assert
        assertNotNull(result);
        verify(sourcePathRepository).findSourcePathsByLibraryItem(tvShow);
    }

    @Test
    void replaceRootOfFilePathWithOtherRoot() throws IOException {
        //Arrest
        Path filePath = this.tvShowDownloads.resolve("filePath.mp4").normalize();
        Files.createFile(filePath);
        Path rootOfFilePath = this.tvShowDownloads.normalize();
        Path newRoot = this.tvShows.normalize();
        //Act
        Path result = sourcePathService.replaceRootOfFilePathWithOtherRoot(filePath, rootOfFilePath, newRoot);
        //Assert
        assertNotNull(result);
        assertEquals(newRoot.resolve("filePath.mp4").normalize(), result);
    }

    @Test
    void getSourcePathsByPathType() {
        //Arrest
        SourcePath.PathType download = SourcePath.PathType.DOWNLOAD;
        when(sourcePathRepository.findSourcePathsByPathType(download)).thenReturn(List.of(this.tvShowDownlaodSourcePath));
        //Act
        List<SourcePath> result = sourcePathService.getSourcePathsByPathType(download);
        //Assert
        assertNotNull(result);
        verify(sourcePathRepository).findSourcePathsByPathType(download);
    }
}