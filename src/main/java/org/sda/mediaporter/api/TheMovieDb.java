package org.sda.mediaporter.api;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TheMovieDb {
    private final ApiConnect theMovieDb;
    @Getter
    private ApiConnect movie;
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private String search;
    private int resultsObjectIndex;

    public TheMovieDb(String search, Integer year) {
        String url = String.format("https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s&year=%s", apiKey, search(search), year);
        theMovieDb = new ApiConnect(url);
        this.resultsObjectIndex = fileIndex(year);
        this.movie = new ApiConnect(String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s", getMovieId(), apiKey));
    }

    private String search(String search){
        this.search = search;
        return search.replace(" ", "+")
                .replace(".", "+");
    }

    public String getImdbId(){
        JSONObject jsonObject = new JSONObject(this.movie.getJsonString());
        System.out.println(jsonObject);
        String key = "imdb_id";
        return jsonObject.getString(key);
    }

    private JSONObject rootObject(){
        return new JSONObject(this.theMovieDb.getJsonString());
    }

    private JSONArray results(){
        String key = "results";
        return rootObject().getJSONArray(key);
    }

    private int fileIndex(Integer year){
        if(year == null){
            return 0;
        }
        for(int i = 0; i < results().length(); i++){
            if(checkYear(i) !=null && !checkYear(i).isEmpty()) {
                if (year == LocalDate.parse(checkYear(i), theMovieDBFormatter()).getYear()) {
                    return i;
                }
            }
        }return 0;
    }

    public int getMovieId(){
        String key = "id";
        return results().getJSONObject(this.resultsObjectIndex).getInt(key);
    }

    public String getOriginalTitle(){
        String key = "original_title";
        try {
            if (results().isEmpty()) {
                return this.search;
            }
            return jsonStringObjectResult(key);
        }catch (JSONException e){
            return null;
        }
    }

    public String getOverview(){
        String key = "overview";
        try {
            return results()
                    .getJSONObject(this.resultsObjectIndex)
                    .getString(key);
        }catch (JSONException e){
            return null;
        }
    }

    private String checkYear(int index){
        String key = "release_date";
        try {
            return results()
                    .getJSONObject(index)
                    .optString(key);
        }catch (JSONException e){
            return null;
        }
    }

    private String realiseDate(){
        String key = "release_date";
        try {
            return results()
                    .getJSONObject(this.resultsObjectIndex)
                    .getString(key);
        }catch (JSONException e){
            return null;
        }
    }

    public LocalDate getReleaseDate(){
        if(realiseDate() == null || realiseDate().isEmpty()) {
            return null;
        }
        return LocalDate.parse(realiseDate(), theMovieDBFormatter());
    }

    public Integer getYear(){
        if(getReleaseDate() == null){
            return null;
        }
        return getReleaseDate().getYear();
    }

    public String getTitle(){
        String key = "title";
        try {
            return jsonStringObjectResult(key);
        }catch (JSONException e){
            return null;
        }
    }

    public List<String> getLanguages(){
        String key = "original_language";
        try {
            String[] languages = jsonStringObjectResult(key)
                    .replace(" ", "")
                    .split(",");
            return Arrays.stream(languages).collect(Collectors.toList());
        }catch (JSONException e){
            return null;
        }
    }

    public Double getRating(){
        String key = "vote_average";
        try {
            return jsonDoubleObjectResult(key);
        }catch (JSONException e){
            return null;
        }
    }

    private DateTimeFormatter theMovieDBFormatter(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public String getPoster(){
        String key = "poster_path";
            String poster = jsonStringObjectResult(key);
            return "https://image.tmdb.org/t/p/w500"+poster;
    }

    public List<String> getGenres(){
        return genreIds()
                .toList() // convert JSONArray to List<Object>
                .stream()
                .map(id -> genreIdsMap()
                        .getOrDefault((Integer) id, "Unknown"))
                .collect(Collectors.toList());
    }
    
    private JSONArray genreIds(){
        String key = "genre_ids";
        try {
            JSONObject movie = results().getJSONObject(resultsObjectIndex);
            return movie
                    .getJSONArray(key);
        }catch (JSONException e){
            return null;
        }
    }

    private Map<Integer, String> genreIdsMap(){
        return Map.ofEntries(
                Map.entry(28, "Action"),
                Map.entry(12, "Adventure"),
                Map.entry(16, "Animation"),
                Map.entry(35, "Comedy"),
                Map.entry(80, "Crime"),
                Map.entry(99, "Documentary"),
                Map.entry(18, "Drama"),
                Map.entry(10751, "Family"),
                Map.entry(14, "Fantasy"),
                Map.entry(36, "History"),
                Map.entry(27, "Horror"),
                Map.entry(10402, "Music"),
                Map.entry(9648, "Mystery"),
                Map.entry(10749, "Romance"),
                Map.entry(878, "Science Fiction"),
                Map.entry(10770, "TV Movie"),
                Map.entry(53, "Thriller"),
                Map.entry(10752, "War"),
                Map.entry(37, "Western")  
        );
    }

    private Integer jsonIntegerObjectResult(String key){
        return  resultsObjectIndexJsonObject()
                .getInt(key);
    }

    private String jsonStringObjectResult(String key){
       return  resultsObjectIndexJsonObject()
                .getString(key);
    }

    private Double jsonDoubleObjectResult(String key){
        return  resultsObjectIndexJsonObject()
                .getDouble(key);
    }

    private boolean checkJsonObjectExists(String key){
        return (resultsObjectIndexJsonObject().has(key) && !resultsObjectIndexJsonObject().isNull(key));
    }

    private JSONObject resultsObjectIndexJsonObject(){
        return results()
                .getJSONObject(this.resultsObjectIndex);
    }

}
