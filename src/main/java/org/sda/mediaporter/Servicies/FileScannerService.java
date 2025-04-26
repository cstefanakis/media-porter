package org.sda.mediaporter.Servicies;

import org.sda.mediaporter.models.Movie;

import java.nio.file.Path;
import java.util.List;

public interface FileScannerService {
    List<Path> files(String path);
    void scannedMoviesPath (String path);
}
