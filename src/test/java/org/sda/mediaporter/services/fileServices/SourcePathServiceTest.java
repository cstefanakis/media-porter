package org.sda.mediaporter.services.fileServices;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SourcePathServiceTest {

    @Autowired
    private SourcePathService sourcePathService;

    @Autowired
    private SourcePathRepository sourcePathRepository;

    private SourcePath sourcePath1;
    private SourcePath sourcePath2;

    @BeforeEach
    void setup(){
        this.sourcePath1 = sourcePathRepository.save(SourcePath.builder()
                        .path("c:/")
                        .pathType(SourcePath.PathType.SOURCE)
                        .libraryItem(LibraryItems.MOVIE)
                .build());

        this.sourcePath2 = sourcePathRepository.save(SourcePath.builder()
                .path("d:/")
                .pathType(SourcePath.PathType.SOURCE)
                .libraryItem(LibraryItems.TV_SHOW)
                .build());

    }


    @Test
    void replaceRootOfFilePathWithOtherRoot_WithSamePathSeparators() {
        //Arrest
        Path filePath = Path.of("C:\\External\\Movies\\file.mkv");
        Path rootFile = Path.of("C:\\External");
        Path newRootFile = Path.of("C:\\Download");
        //Act
        Path result = sourcePathService.replaceRootOfFilePathWithOtherRoot(filePath, rootFile, newRootFile);
        //Assert
        assertEquals(Path.of("C:\\Download\\file.mkv"), result);
    }

    @Test
    void replaceRootOfFilePathWithOtherRoot_WithDifferentSeparators() {
        //Arrest
        Path filePath = Path.of("C:\\External\\Movies\\file.mkv");
        Path rootFile = Path.of("C:\\External");
        Path newRootFile = Path.of("C:/Download");
        //Act
        Path result = sourcePathService.replaceRootOfFilePathWithOtherRoot(filePath, rootFile, newRootFile);
        //Assert
        assertEquals(Path.of("C:\\Download\\file.mkv"), result);
    }

    @Test
    void getSourcePathsByPathType() {
        //Act
        Long sourcePath1Id = this.sourcePath1.getId();
        Long sourcePath2Id = this.sourcePath2.getId();
        //act
        List<SourcePath> result = sourcePathService.getSourcePathsByPathType(SourcePath.PathType.SOURCE);
        //Assert
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(sp -> sp.getId().equals(sourcePath1Id)));
        assertTrue(result.stream().anyMatch(sp -> sp.getId().equals(sourcePath2Id)));
    }

    @AfterEach
    void end(){
        sourcePathRepository.delete(sourcePath1);
        sourcePathRepository.delete(sourcePath2);
    }
}