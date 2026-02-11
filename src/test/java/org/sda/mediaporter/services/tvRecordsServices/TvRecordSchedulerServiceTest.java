package org.sda.mediaporter.services.tvRecordsServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.sda.mediaporter.models.Configuration;
import org.sda.mediaporter.models.SourcePath;
import org.sda.mediaporter.models.enums.LibraryItems;
import org.sda.mediaporter.repositories.ConfigurationRepository;
import org.sda.mediaporter.repositories.SourcePathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class TvRecordSchedulerServiceTest {

    @Autowired
    private SourcePathRepository sourcePathRepository;

    @Autowired
    private TvRecordSchedulerService tvRecordSchedulerService;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @TempDir
    Path tvShowsRecSource;

    @TempDir
    Path tvShowRecExternal;

    @Test
    void copyMoviesFromTvRecordSources() {
    }

    @Test
    void copyTvShowsFromTvRecordSources() throws IOException {
        //Arrest
        Path popMageiriki = this.tvShowRecExternal.resolve("Ποπ Μαγειρική - Θ Κύκλος[K].Ψυχαγωγία (Γαστρονομία) «Ζήσης Ρούμπος - Σπετσερικό Κέρκυρας, Καρύδια Δομοκού»2026-02-0414-00").normalize();
        Files.createFile(popMageiriki);

        Path hotelElvira = tvShowRecExternal.resolve("[K8] Hoteλ Ελvira2026-01-3117-20").normalize();
        Files.createFile(hotelElvira);

        Configuration sourceConfiguration = configurationRepository.save(Configuration.builder()
                        .maxDatesControlFilesFromExternalSource(1000)
                        .maxDatesSaveFile(null)
                .build());

        Configuration externalConfiguration = configurationRepository.save(Configuration.builder()
                .maxDatesControlFilesFromExternalSource(1000)
                .maxDatesSaveFile(null)
                .build());

        SourcePath tvShowsRecSourceSourcePath = sourcePathRepository.save(SourcePath.builder()
                .path(this.tvShowsRecSource.toString())
                .libraryItem(LibraryItems.TV_SHOW_RECORDS)
                .pathType(SourcePath.PathType.SOURCE)
                .title("Tv Shows recs Source")
                        .configuration(sourceConfiguration)
                .build());

        SourcePath tvShowsRecExternalSourcePath = sourcePathRepository.save(SourcePath.builder()
                .path(this.tvShowRecExternal.toString())
                .libraryItem(LibraryItems.TV_SHOW)
                .pathType(SourcePath.PathType.TV_RECORDS)
                .title("Tv Shows recs external")
                        .configuration(externalConfiguration)
                .build());
        //Act
        tvRecordSchedulerService.copyTvShowsFromTvRecordSources();
        //Assert
        Files.walk(this.tvShowsRecSource).forEach(System.out::println);

    }

    @Test
    void deleteOldTvRecords() {
    }
}