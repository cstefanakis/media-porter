package org.sda.mediaporter.Services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private FileService fileService;

    private Path downloadPath = Path.of("src/test/resources/movies/downloads");
    private Path moviesPath = Path.of("src/test/resources/movies/movies");
    private Path externalPath = Path.of("src/test/resources/movies/external");
    private Path supermanFile =Path.of("src/test/resources/movies/downloads/superman (2025)/superman (2025) Trailer.mp4");

    @BeforeEach
    void setup(){
        sourcePathRepository.deleteAll();
        sourcePathRepository.save(SourcePath.builder()
                        .path(downloadPath.toString())
                        .title("Downloads")
                        .pathType(SourcePath.PathType.DOWNLOAD)
                        .libraryItem(LibraryItems.MOVIE)
                .build());

        sourcePathRepository.save(SourcePath.builder()
                        .path(moviesPath.toString())
                        .title("movies")
                        .pathType(SourcePath.PathType.SOURCE)
                        .libraryItem(LibraryItems.MOVIE)
                .build());

        sourcePathRepository.save(SourcePath.builder()
                .path(externalPath.toString())
                .title("movies")
                .pathType(SourcePath.PathType.EXTERNAL)
                .libraryItem(LibraryItems.MOVIE)
                .build());

        Path filePath = Path.of("src/test/resources/movies/notitled (720p_24fps_H264-128kbit_AAC).mp4");
        Path destinationFilePath = Path.of("src/test/resources/movies/downloads/superman (2025)/superman (2025) Trailer.mp4");
        try {
            Files.createDirectory(Path.of("src/test/resources/movies/downloads/superman (2025)"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileService.copyFile(filePath, destinationFilePath);
    }

    @Test
    void getVideoFiles() {
        //Act
        List<Path> files = fileService.getVideoFiles(this.downloadPath);
        //Assert
        assertEquals(1, files.size());
    }

    @Test
    void copyFile() {
        //Arrest
        Path destinationFilePath = Path.of(this.moviesPath + File.separator + "superman (2025) Trailer.mp4");
        //Act
        fileService.copyFile(this.supermanFile, destinationFilePath);
        List<Path> movieFiles = fileService.getVideoFiles(this.moviesPath);

        //Assert
        assertEquals(1, movieFiles.size());
        assertEquals("superman (2025) Trailer.mp4", movieFiles.getFirst().getFileName().toString());
    }

    @Test
    void deleteFile() {
        //Arrest
        List<Path> files = fileService.getVideoFiles(this.downloadPath);
        assertEquals(1, files.size());
        //Act
        fileService.deleteFile(this.supermanFile, "superman (2025)");
        //Assert
        files = fileService.getVideoFiles(this.downloadPath);
        assertEquals(0,  files.size());

    }

    @Test
    void moveFile() {
        //Arrest
        Path destinationFilePath = Path.of("src/test/resources/movies/movies/superman (2025) Trailer.mp4");

        //Act
        fileService.moveFile(this.supermanFile, destinationFilePath);
        List<Path> files = fileService.getVideoFiles(this.moviesPath);

        //Assert
        assertEquals(1, files.size());
        assertEquals("superman (2025) Trailer.mp4", files.getFirst().getFileName().toString());
    }

    @Test
    void renameFile() {
        //Arrest
        String newName = "Batman (2022)";
        String[] directories = new String[] {"Batman (2022)"};
        //Act
        fileService.renameFile(this.supermanFile, newName, directories);
        List<Path> files = fileService.getVideoFiles(this.downloadPath);
        //Assert
        assertTrue(Files.exists(Path.of("src/test/resources/movies/downloads/Batman (2022)/Batman (2022).mp4")));
        assertFalse(Files.exists(Path.of("src/test/resources/movies/downloads/superman (2025)/superman (2025)")));
        assertFalse(Files.exists(Path.of("src/test/resources/movies/downloads/superman (2025)")));
    }

    @Test
    void getFileExtensionWithDot() {
        //Act
        String extension = fileService.getFileExtensionWithDot(this.supermanFile);
        //Assert
        assertEquals(".mp4", extension);
    }

    @Test
    void createdDirectories() {
        //Arrest
        String[] directories = new String[] {"Batman (2021)", "Season 01"};
        Path createdDirPath = Path.of(this.externalPath + File.separator + "Batman (2021)" + File.separator + "Season 01");
        //Act
        Path newPath = fileService.createdDirectories(externalPath, directories);
        //Assert
        assertTrue(Files.exists(createdDirPath));
        assertEquals(createdDirPath, newPath);
    }

    @Test
    void getModificationLocalDateTimeOfPath() {
        //Arrest
        LocalDateTime nowLDT = LocalDateTime.now();
        FileTime now = fileService.localDateTimeToFileTime(nowLDT);
        try {
            Files.setLastModifiedTime(this.supermanFile, now);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Act
        LocalDateTime fileModificationDate = fileService.getModificationLocalDateTimeOfPath(this.supermanFile);
        //Assert
        assertEquals(nowLDT, fileModificationDate);
    }

    @AfterEach
    void after(){
        List<Path> directories = List.of(this.downloadPath, this.moviesPath, this.externalPath);
        directories.forEach(d -> {
            try {
                Files.walk(d)
                        .sorted(Comparator.reverseOrder())
                        .forEach(f -> {
                            try {
                                Files.delete(f);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                Files.createDirectory(d);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}