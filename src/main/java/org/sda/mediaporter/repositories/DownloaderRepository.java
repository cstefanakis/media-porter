package org.sda.mediaporter.repositories;

import org.sda.mediaporter.models.DownloadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloaderRepository extends JpaRepository<DownloadFile, Long> {

    @Query("select df from DownloadFile df where df.libraryItems = :libraryItem and df.status = :status")
    List<DownloadFile> getDownloadFileByLibraryItemAndStatus(
            @Param("libraryItem")String libraryItem,
            @Param("status") String status);

}
