package org.sda.mediaporter.api;

import org.json.JSONObject;
import org.sda.mediaporter.models.Genre;
import org.sda.mediaporter.models.Movie;
import org.sda.mediaporter.models.TvShow;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OmdbApi {
    private ApiConnect omdbApi;
    private final String apiKey = "c30304ec";

    public OmdbApi(String movieTitle, Integer movieYear) {
        this.omdbApi = new ApiConnect(url(movieTitle, movieYear));
    }

    private String url(String movieTitle, Integer movieYear){
        if(movieYear == null){
            return String.format("https://www.omdbapi.com/?t=%s&apikey=%s", search(movieTitle), this.apiKey);
        }
        return String.format("https://www.omdbapi.com/?t=%s+&y=%s&apikey=%s", search(movieTitle), movieYear, this.apiKey);
    }

    public OmdbApi(String tvShowTitle, Integer seasonNumber, Integer episodeNumber) {
        String url = String.format("https://www.omdbapi.com/?t=%s&Season=%s&Episode=%s+&apikey=%s", search(tvShowTitle), seasonNumber, episodeNumber, this.apiKey);
    }

    private String search(String search){
        return URLEncoder.encode(search, StandardCharsets.UTF_8);
    }

    private JSONObject rootObject(){
        return new JSONObject(omdbApi.getJsonString());
    }

    public String getTitle(){
        return rootObject().getString("Title");
    }

    public Integer getYear(){
        return rootObject().getInt("Year");
    }

    public LocalDate getReleasedDate(){
        String date = rootObject().getString("Released");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return LocalDate.parse(date, formatter);
    }

    public List<String> getGenre(){
        List<String> genres = new ArrayList<>();
        String[] apiGenres = rootObject().getString("Genre")
                .replace(" ", "")
                .split(",");
        Collections.addAll(genres, apiGenres);
        return genres;
    }

    public List<String> getDirector(){
        String[] directors =  rootObject().getString("Director")
                .split(",");
        return Arrays.stream(directors).toList();
    }
    public List<String> getWriter(){
        String[] writers = rootObject().getString("Writer")
                .replace(", ", ",")
                .split(",");
        return Arrays.stream(writers).toList();
    }

    public List<String> getActors(){
        String[] actors =  rootObject().getString("Actors")
                .replace(", ", ",")
                .split(",");
        return Arrays.stream(actors).toList();
    }

    public String getPlot(){
        return rootObject().getString("Plot");
    }

    public List<String> getLanguages(){
        String[] languages = rootObject().getString("Language")
                .replace(" ", "")
                .split(",");
        return Arrays.stream(languages).toList();
    }

    public String getCountry(){
        return  rootObject().getString("Country");
    }

    public String getPoster(){
        return  rootObject().getString("Poster");
    }
    public Double getImdbRating(){
        return  rootObject().getDouble("imdbRating");
    }

    public String getType(){
        return  rootObject().getString("Type");
    }

    public String getBoxOffice(){
        return  rootObject().getString("BoxOffice");
    }

}
