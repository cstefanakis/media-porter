package org.sda.mediaporter.Servicies.Impl;

import org.json.JSONException;
import org.sda.mediaporter.Servicies.FileScannerService;
import org.sda.mediaporter.Servicies.MovieService;
import org.sda.mediaporter.models.Movie;
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
public class FileScannerServiceImpl implements FileScannerService {

    private final MovieService movieService;

    public FileScannerServiceImpl(MovieService movieService) {
        this.movieService = movieService;
    }

    private Integer year;
    private Movie movie = new Movie();

    @Override
    public List<Path> files(String path){
        return videoFiles(path);
    }

    @Override
    public List<Movie> scannedMoviesPath(String path) {
        List<Movie> movies = new ArrayList<>();
        List<Path> files = videoFiles(path);
        for (Path file : files) {
            if(checkMovie(title(file.getFileName().toString()))){
                this.movie.setPath(file.toString().replace(path, ""));
            }
            movies.add(movie);
            System.out.println(movie);
            System.out.println("saved year: "+this.year);
        }
        return movies;
    }

    //try to find movie in api
    private boolean checkMovie(String[] titleElements) {
        for (int i = titleElements.length-1; i > 0; i--) {
            String[] subArray = Arrays.copyOfRange(titleElements, 0, i);
            try{
                String title = String.join(" ",subArray);
                System.out.println("title: " + title + " year: " + this.year);
                this.movie = movieService.getMovieByTitle(title, this.year);
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
                    !resolutionMatches(element) && !noTitleWords(element)){
                titleElements.add(element);
            }
            if(year(element) != null){
                this.year = year(element);
            }
        }return titleElements.toArray(new String[0]);
    }

    //words that can't be title
    private boolean noTitleWords(String filename){
        String [] words = {"cz", "cze", "en", "eng", "dabing", "czdab"};
        for(String element : words){
            if(filename.equalsIgnoreCase(element)){
                return true;
            }
        }return false;
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
