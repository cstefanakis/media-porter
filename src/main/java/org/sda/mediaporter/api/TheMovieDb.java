package org.sda.mediaporter.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TheMovieDb {
    private final ApiConnect theMovieDb;
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private String search;
    private int resultsObjectIndex;

    public TheMovieDb(String search, Integer year) {
        String url = String.format("https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s", apiKey, search(search));
        theMovieDb = new ApiConnect(url);
        this.resultsObjectIndex = fileIndex(year);
    }

    public TheMovieDb(String contributorName) {
        String url = String.format("https://api.themoviedb.org/3/search/person?query=%s&api_key=%s", search(contributorName), apiKey);
        theMovieDb = new ApiConnect(url);
    }

    private String search(String search){
        this.search = search;
        return search.replace(" ", "+")
                .replace(".", "+");
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
            if(year == LocalDate.parse(realiseDate(), theMovieDBFormatter()).getYear()){
                return i;
            }
        }
        return 0;
    }

    public String getOriginalTitle(){
        String key = "original_title";
        if (results().isEmpty()) {
            return this.search;
        }
        return jsonStringObjectResult(key);
    }

    public String getOverview(){
        String key = "overview";
        return results()
                .getJSONObject(0)
                .getString("overview");
    }

    private String realiseDate(){
        String key = "release_date";
        return jsonStringObjectResult(key);
    }

    public LocalDate getReleaseDate(){
        return LocalDate.parse(realiseDate(), theMovieDBFormatter());
    }

    public Integer getYear(){
        return getReleaseDate().getYear();
    }

    public String getTitle(){
        String key = "title";
        return jsonStringObjectResult(key);
    }

    public List<String> getLanguages(){
        String key = "original_language";
        String[] languages =jsonStringObjectResult(key)
                .replace(" ", "")
                .split(",");
        return Arrays.stream(languages).collect(Collectors.toList());
    }

    public Double getRating(){
        String key = "vote_average";
        return jsonDoubleObjectResult(key);
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
        JSONObject movie = results().getJSONObject(resultsObjectIndex);
        return movie
                .getJSONArray(key);
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

    public String getContributorName(){
        String key = "original_name";
        return jsonStringObjectResult(key);
    }

    public String getContributorPoster(){
        String key = "profile_path";
        if(checkJsonObjectExists(key)) {
            String path = jsonStringObjectResult(key);
            return "https://image.tmdb.org/t/p/w500"+path;
        }
        return null;

    }

    public String getContributorWebsite(){
        String key = "id";
        if(checkJsonObjectExists(key)) {
            int id = jsonIntegerObjectResult(key);
            return "https://www.themoviedb.org/person/" + id;
        }return null;
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
