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
    private ApiConnect theMovieDb;
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
        return rootObject().getJSONArray("results");
    }

    private int fileIndex(Integer year){
        if(year == null){
            return 0;
        }
        for(int i = 0; i < results().length(); i++){
            if(year == LocalDate.parse(realiseDate(i), theMovieDBFormatter()).getYear()){
                return i;
            }
        }
        return 0;
    }

    public String getOriginalTitle(){
        if (results().isEmpty()) {
            return this.search;
        }
        return resultsIndexJsonObject(0)
                .getString("original_title");
    }

    private JSONObject resultsIndexJsonObject(int index){
        return results().getJSONObject(index);
    }

    public String getOverview(){
        return results()
                .getJSONObject(0)
                .getString("overview");
    }

    private String realiseDate(int index){
        return resultsIndexJsonObject(index).getString("release_date");
    }

    public LocalDate getReleaseDate(){
        return LocalDate.parse(realiseDate(this.resultsObjectIndex), theMovieDBFormatter());
    }

    public Integer getYear(){
        return getReleaseDate().getYear();
    }

    public String getTitle(){
        return resultsIndexJsonObject(this.resultsObjectIndex)
                .getString("title");
    }

    public List<String> getLanguages(){
        String[] languages =resultsIndexJsonObject(this.resultsObjectIndex)
                .getString("original_language")
                .replace(" ", "")
                .split(",");
        return Arrays.stream(languages).collect(Collectors.toList());
    }

    public Double getRating(){
        return resultsIndexJsonObject(this.resultsObjectIndex)
                .getDouble("vote_average");
    }

    private DateTimeFormatter theMovieDBFormatter(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public String getPoster(){
        String poster = results()
                .getJSONObject(0)
                .getString("poster_path");
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
        JSONObject movie = results().getJSONObject(0);
        return movie
                .getJSONArray("genre_ids");
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
        return results().getJSONObject(0)
                .getString("original_name");
    }

    public String getContributorPoster(){
        String path =  results().getJSONObject(0)
                .getString("profile_path");
        return "https://image.tmdb.org/t/p/w500"+path;
    }

    public String getContributorWebsite(){
        int id = results().getJSONObject(0)
                .getInt("id");
        return "https://www.themoviedb.org/person/"+id;
    }

}
