package org.sda.mediaporter.services.fileServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.services.fileServices.impl.SourcePathServiceImpl;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SourcePathServiceTest {

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
    void getById() {
        //
    }

    @Test
    void getSourcePaths() {
    }

    @Test
    void getSourcePathsByPathTypeAndLibraryItem() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void getRootFileFromFile() {
    }

    @Test
    void createSourcePath() {
    }

    @Test
    void updateSourcePath() {
    }

    @Test
    void getSourcePathFromPath() {
    }

    @Test
    void getSourcePathsByLibraryItem() {
    }

    @Test
    void replaceRootOfFilePathWithOtherRoot() {
    }

    @Test
    void getSourcePathsByPathType() {
    }
}