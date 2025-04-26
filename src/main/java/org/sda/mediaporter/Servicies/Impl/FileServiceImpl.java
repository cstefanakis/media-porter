package org.sda.mediaporter.Servicies.Impl;

import org.json.JSONException;
import org.sda.mediaporter.Servicies.FileService;
import org.sda.mediaporter.Servicies.MovieService;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final MovieService movieService;
    private final MovieRepository movieRepository;

    public FileServiceImpl(MovieService movieService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    private Integer year;
    private Movie movie = new Movie();

    @Override
    public List<Path> files(String path){
        return videoFiles(path);
    }

    @Override
    public void scannedMoviesPath(String path) {
        List<Path> files = videoFiles(path);
        for (Path file : files) {
            if (checkMovie(title(file.getFileName().toString()))) {
                this.movie.setPath(file.toString().replace(path, ""));
            }
            movieRepository.save(movie);
        }
    }

    @Override
    public void copyFile(Path fromPath, Path toPath) {
            if(Files.exists(toPath) && isSameSizeBetweenTowFiles(fromPath,toPath)){
                try {
                    Files.copy(fromPath, toPath);
                } catch (IOException e) {
                    throw new RuntimeException(String.format("Failed to copy file %s to %s", fromPath, toPath));
                }
            }
    }

    private boolean isSameSizeBetweenTowFiles(Path file1, Path file2)  {
        try {
            if (Files.size(file1) ==  Files.size(file2)){
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    @Override
    public void deleteFile(Path path) {
        try {
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException(String.format("Failed to delete %s", path));
        }
    }

    @Override
    public void moveFile(Path fromPath, Path pathWithoutFileName) {
        Path newFullPath = pathWithoutFileName.resolve(fromPath);
        if(!Files.exists(newFullPath)){
            try {
                Files.copy(fromPath, newFullPath);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to move file %s to %s", fromPath, newFullPath));
            }
        }
    }

    @Override
    public void renameFile(Path filePath, String newName) {
        String newNameWithExtension = newName.trim() + getFileExtensionWithDot(filePath);
            try {
                Files.move(filePath, filePath.resolveSibling(newName + newNameWithExtension));
            } catch (IOException e) {
                throw new RuntimeException(String.format("Failed to rename file %s to %s", filePath.getFileName(), newNameWithExtension));
            }
    }

    private String getFileExtensionWithDot(Path file) {
        int dotIndex = file.getFileName().toString().lastIndexOf(".");
        if(dotIndex > 0){
            return file.getFileName().toString().substring(dotIndex);
        }
        return "";
    }

    //try to find movie in api
    private boolean checkMovie(String[] titleElements) {
        for (int i = titleElements.length-1; i > 0; i--) {
            String[] subArray = Arrays.copyOfRange(titleElements, 0, i);
            try{
                String title = String.join(" ",subArray);
                System.out.println("title: " + title + " year: " + this.year);
                this.movie = movieService.getMovieFromApiByTitle(title, this.year);
                return true;
            }catch (JSONException ignored){

            }
        }
        return false;
    }

    //create filtered splitted filename without year, resolution. no title words
    private String[] title(String filename){
        this.year = null;
        List<String> titleElements = new ArrayList<>();
        for (int i = 0; i < splittedFilename(filename).length; i++) {
            String element = splittedFilename(filename)[i];
            if(!yearMatched(element) &&
                    !resolutionMatches(element) &&
                    !elementFilter(noTitleWords(), element) &&
                    !elementFilter(codecs(),element) &&
                    !elementFilter(languageCodes(), element) &&
                    !elementFilter(videoExtensions(), element)
            ){
                titleElements.add(element);
            }
            if(year(element) != null){
                this.year = year(element);
            }
        }return titleElements.toArray(new String[0]);
    }

    //words that can't be title
    private boolean elementFilter(String[] ignoredStrings, String fileElement){
        for(String element : ignoredStrings){
            if(fileElement.trim().equalsIgnoreCase(element)){
                return true;
            }
        }return false;
    }

    private String[] noTitleWords(){
        return new String[] {"dabing", "czdab", "genres"};
    }

    private String[] codecs(){
        return new String[] {"H.264","x264","AVC", "H.265", "AV1", "VP9", "VP8", "MPEG-2", "MPEG-4", "Xvid", "DivX","EAC3", "AAC", "MP3", "FLAC", "ALAC", "Opus", "Vorbis", "PCM", "WMA", "AC-3","AC3", "DTS"};
    }

    private String[] languageCodes(){
        return new String[] {"en", "es", "fr", "de", "it", "pt", "ru", "zh", "ja", "ko",
                "ar", "hi", "tr", "pl", "nl", "sv", "fi", "no", "da", "el",
                "cs", "ro", "hu", "th", "id", "he", "uk", "vi", "ms", "fa", "bn",
                "eng", "spa", "fra", "deu", "ita", "por", "rus", "zho", "jpn", "kor",
                "ara", "hin", "tur", "pol", "nld", "swe", "fin", "nor", "dan", "ell",
                "ces", "ron", "hun", "tha", "ind", "heb", "ukr", "vie", "msa", "fas", "ben"};
    }


    //find resolution
    private boolean resolutionMatches(String resolution){
        return resolution.toLowerCase().matches("^[0-9].*p$");
    }

    //find year
    private boolean yearMatched(String year){
        return year.matches("^[0-9]{4}$");
    }

    //get year if string element is integer between 1900 and this year + 2
    private Integer year(String element){
        try {
            int parsed = Integer.parseInt(element);
            int currentYear = LocalDate.now().getYear();
            if (parsed > 1900 && parsed < currentYear + 2) {
                return parsed;
            }
        } catch (NumberFormatException ignored) {}
        return null;
    }

    //replace special characters with space and split filename by space
    private String[] splittedFilename(String filename){
        return filename
                .replaceAll("[!@#$%^&*()\\-_+=\\{\\}\\[\\]:;\"',.<>?/\\\\|+\\-*/%~^€©™®]", " ")
                .trim()
                .split("\\s+");
    }

    //scan video files
    private List<Path> videoFiles(String path){
        try{
            return Files.walk(Paths.get(path))
                    .filter(f -> {
                        String fileName = f.getFileName().toString();
                        for (String ext : videoExtensions()) {
                            if (fileName.toLowerCase().endsWith(ext)) {
                                System.out.println(fileName);
                                return true;
                            }
                        }
                        return false;
                    }).toList();
        }catch (IOException e){
            return List.of();
        }
    }

    private String[] videoExtensions(){
        return new String[] {".mp4",".mkv",".avi",".mov",".wmv",".flv",".webm",".mpeg",".mpg",".m4v",".3gp",".ts",".vob"};
    }

}
