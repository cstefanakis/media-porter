package org.sda.mediaporter.api;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
            if (date != null && !date.isEmpty() && !date.equalsIgnoreCase("N/A")){
                return LocalDate.parse(date, formatter);
            }
            return null;

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
        if(rootObject().has("Director")) {
            String[] directors = rootObject().getString("Director")
                    .split(",");
            return Arrays.stream(directors).toList();
        }
        return Collections.emptyList();
    }
    public List<String> getWriter(){
        if(rootObject().has("Writer")) {
            String[] writers = rootObject().getString("Writer")
                    .replace(", ", ",")
                    .split(",");
            return Arrays.stream(writers).toList();
        }
        return Collections.emptyList();
    }

    public List<String> getActors(){
        if(rootObject().has("Actors")){
            String[] actors =  rootObject().getString("Actors")
                    .replace(", ", ",")
                    .split(",");
            return Arrays.stream(actors).toList();
        }
        return Collections.emptyList();
    }

    public String getPlot(){
        if(rootObject().has("Plot")){
            return rootObject().getString("Plot");
        }
        return null;
    }

    public List<String> getLanguages(){
            String[] languages = rootObject().getString("Language")
                    .replace(" ", "")
                    .split(",");
            return Arrays.asList(languages);
    }

    public String getCountry(){
        if(rootObject().has("Country")) {
            return rootObject().getString("Country");
        }
        return null;
    }

    public String getPoster(){
        if(rootObject().has("Poster")) {
            return rootObject().getString("Poster");
        }
        return null;
    }
    public Double getImdbRating(){
        if(rootObject().has("imdbRating")){
            String rate = rootObject().getString("imdbRating");
            if (rate != null && !rate.isEmpty() && !rate.equalsIgnoreCase("N/A")){
                return rootObject().getDouble("imdbRating");
            }
            return  null;
        }
        return null;
    }

    public String getType(){
        if(rootObject().has("Type")){
            return  rootObject().getString("Type");
        }
        return null;
    }

    public String getBoxOffice(){
        if(rootObject().has("BoxOffice")){
            return  rootObject().getString("BoxOffice");
        }
        return null;
    }

}
