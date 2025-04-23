package org.sda.mediaporter.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TheMovieDb {
    private ApiConnect theMovieDb;
    private final String apiKey = "7c97b163195d9428522398e8f1c32f63";
    private String search;

    public TheMovieDb(String search) {
        String url = String.format("https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s", apiKey, search(search));
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

    public String getOriginalTitle(){
        if (results().isEmpty()) {
            return this.search;
        }
        return results()
                .getJSONObject(0)
                .getString("original_title");
    }

    public String getOverview(){
        return results()
                .getJSONObject(0)
                .getString("overview");
    }

    public LocalDate getReleaseDate(){
        String date = results()
                .getJSONObject(0)
                .getString("release_date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return LocalDate.parse(date, formatter);
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
}
